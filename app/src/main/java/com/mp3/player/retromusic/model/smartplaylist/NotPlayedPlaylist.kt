package com.mp3.player.retromusic.model.smartplaylist

import com.mp3.player.retromusic.App
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class NotPlayedPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.not_recently_played),
    iconRes = R.drawable.ic_audiotrack
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.notRecentlyPlayedTracks()
    }
}