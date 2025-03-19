/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package com.mp3.player.retromusic.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.contains
import androidx.navigation.ui.setupWithNavController
import com.mp3.player.retromusic.BuildConfig
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.activities.base.AbsCastActivity
import com.mp3.player.retromusic.ads.AdmobLib
import com.mp3.player.retromusic.extensions.*
import com.mp3.player.retromusic.helper.MusicPlayerRemote
import com.mp3.player.retromusic.helper.SearchQueryHelper.getSongs
import com.mp3.player.retromusic.interfaces.IScrollHelper
import com.mp3.player.retromusic.model.CategoryInfo
import com.mp3.player.retromusic.model.Song
import com.mp3.player.retromusic.repository.PlaylistSongsLoader
import com.mp3.player.retromusic.service.MusicService
import com.mp3.player.retromusic.util.AppRater
import com.mp3.player.retromusic.util.JsonParams
import com.mp3.player.retromusic.util.PreferenceUtil
import com.mp3.player.retromusic.util.RemoteJSONSource
import com.mp3.player.retromusic.util.logE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.logger.Logger

class MainActivity : AbsCastActivity() {
    val DEBUG: Boolean = BuildConfig.BUILD_TYPE != "release"
    var isAppReady = false
    companion object {
        const val TAG = "MainActivity"
        const val EXPAND_PANEL = "expand_panel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Giữ Splash cho đến khi load xong
//        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        start()
//        splashScreen.setKeepOnScreenCondition { AdmobLib.getInstance(this).getAdClose() == true && isAppReady == true }
//        // Giả lập tải dữ liệu
//        lifecycleScope.launch {
//            loadAds()
//            waitForAdClose()
//            delay(500)
//
//        }


    }

    private fun start() {
        setTaskDescriptionColorAuto()
        hideStatusBar()
        updateTabs()
        AppRater.appLaunched(this)

        setupNavigationController()

        WhatsNewFragment.showChangeLog(this)
    }

    private suspend fun waitForAdClose() {
        while (AdmobLib.getInstance(this).getAdClose() != true) {
            delay(500) // Kiểm tra lại sau 500ms
        }
    }

    private suspend fun loadAds() {
//        withContext(Dispatchers.IO) {
            if (JsonParams.DATA == null) {
                try {
                    RemoteJSONSource(this).execute("")
                } catch (e: Exception) {
                    if (DEBUG) Log.e(TAG, "Error ads...${e.message}")
                }
            } else {
            }
//        }
//        withContext(Dispatchers.Main) {
//            start()
//            isAppReady = true
//        }
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.fragment_container)
//        findViewById<FragmentContainerView>(R.id.fragment_container).visibility = View.GONE

        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.main_graph)

        val categoryInfo: CategoryInfo = PreferenceUtil.libraryCategory.first { it.visible }
        if (categoryInfo.visible) {
            if (!navGraph.contains(PreferenceUtil.lastTab)) PreferenceUtil.lastTab =
                categoryInfo.category.id
            navGraph.setStartDestination(
                if (PreferenceUtil.rememberLastTab) {
                    PreferenceUtil.lastTab.let {
                        if (it == 0) {
                            categoryInfo.category.id
                        } else {
                            it
                        }
                    }
                } else categoryInfo.category.id
            )
        }
        navController.graph = navGraph
        navigationView.setupWithNavController(navController)
        // Scroll Fragment to top
        navigationView.setOnItemReselectedListener {
            currentFragment(R.id.fragment_container).apply {
                if (this is IScrollHelper) {
                    scrollToTop()
                }
            }
        }
        navigationView.visibility = View.VISIBLE

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == navGraph.startDestinationId) {
                currentFragment(R.id.fragment_container)?.enterTransition = null
            }
            when (destination.id) {
                R.id.action_home, R.id.action_song, R.id.action_album, R.id.action_artist, R.id.action_folder, R.id.action_playlist, R.id.action_genre, R.id.action_search -> {
                    // Save the last tab
                    if (PreferenceUtil.rememberLastTab) {
                        saveTab(destination.id)
                    }
                    // Show Bottom Navigation Bar
                    setBottomNavVisibility(visible = true, animate = true)
                }
                R.id.playing_queue_fragment -> {
                    setBottomNavVisibility(visible = false, hideBottomSheet = true)
                }
                else -> setBottomNavVisibility(
                    visible = false,
                    animate = true
                ) // Hide Bottom Navigation Bar
            }
        }
    }

    private fun saveTab(id: Int) {
        if (PreferenceUtil.libraryCategory.firstOrNull { it.category.id == id }?.visible == true) {
            PreferenceUtil.lastTab = id
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val expand = intent?.extra<Boolean>(EXPAND_PANEL)?.value ?: false
        if (expand && PreferenceUtil.isExpandPanel) {
            fromNotification = true
            slidingPanel.bringToFront()
            expandPanel()
            intent?.removeExtra(EXPAND_PANEL)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        intent ?: return
        handlePlaybackIntent(intent)
    }

    private fun handlePlaybackIntent(intent: Intent) {
        lifecycleScope.launch(IO) {
            val uri: Uri? = intent.data
            val mimeType: String? = intent.type
            var handled = false
            if (intent.action != null &&
                intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
            ) {
                val songs: List<Song> = getSongs(intent.extras!!)
                if (MusicPlayerRemote.shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) {
                    MusicPlayerRemote.openAndShuffleQueue(songs, true)
                } else {
                    MusicPlayerRemote.openQueue(songs, 0, true)
                }
                handled = true
            }
            if (uri != null && uri.toString().isNotEmpty()) {
                MusicPlayerRemote.playFromUri(this@MainActivity, uri)
                handled = true
            } else if (MediaStore.Audio.Playlists.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "playlistId", "playlist")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs: List<Song> = PlaylistSongsLoader.getPlaylistSongList(get(), id)
                    MusicPlayerRemote.openQueue(songs, position, true)
                    handled = true
                }
            } else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "albumId", "album")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs = libraryViewModel.albumById(id).songs
                    MusicPlayerRemote.openQueue(
                        songs,
                        position,
                        true
                    )
                    handled = true
                }
            } else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
                val id = parseLongFromIntent(intent, "artistId", "artist")
                if (id >= 0L) {
                    val position: Int = intent.getIntExtra("position", 0)
                    val songs: List<Song> = libraryViewModel.artistById(id).songs
                    MusicPlayerRemote.openQueue(
                        songs,
                        position,
                        true
                    )
                    handled = true
                }
            }
            if (handled) {
                setIntent(Intent())
            }
        }
    }

    private fun parseLongFromIntent(
        intent: Intent,
        longKey: String,
        stringKey: String,
    ): Long {
        var id = intent.getLongExtra(longKey, -1)
        if (id < 0) {
            val idString = intent.getStringExtra(stringKey)
            if (idString != null) {
                try {
                    id = idString.toLong()
                } catch (e: NumberFormatException) {
                    logE(e)
                }
            }
        }
        return id
    }
}
