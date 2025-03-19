package com.mp3.player.retromusic.interfaces

import android.view.View
import com.mp3.player.retromusic.db.PlaylistWithSongs

interface IPlaylistClickListener {
    fun onPlaylistClick(playlistWithSongs: PlaylistWithSongs, view: View)
}