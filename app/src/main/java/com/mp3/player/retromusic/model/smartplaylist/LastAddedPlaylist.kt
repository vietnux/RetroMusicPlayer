package com.mp3.player.retromusic.model.smartplaylist

import com.mp3.player.retromusic.App
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class LastAddedPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.last_added),
    iconRes = R.drawable.ic_library_add
) {
    override fun songs(): List<Song> {
        return lastAddedRepository.recentSongs()
    }
}