package com.mp3.player.retromusic.model.smartplaylist

import com.mp3.player.retromusic.App
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class ShuffleAllPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.action_shuffle_all),
    iconRes = R.drawable.ic_shuffle
) {
    override fun songs(): List<Song> {
        return songRepository.songs()
    }
}