package com.mp3.player.retromusic.model.smartplaylist

import com.mp3.player.retromusic.App
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.Song
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent

@Parcelize
class HistoryPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.history),
    iconRes = R.drawable.ic_history
), KoinComponent {

    override fun songs(): List<Song> {
        return topPlayedRepository.recentlyPlayedTracks()
    }
}