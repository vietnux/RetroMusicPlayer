package com.mp3.player.retromusic.model.smartplaylist

import com.mp3.player.retromusic.App
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.Song
import kotlinx.parcelize.Parcelize

@Parcelize
class TopTracksPlaylist : AbsSmartPlaylist(
    name = App.getContext().getString(R.string.my_top_tracks),
    iconRes = R.drawable.ic_trending_up
) {
    override fun songs(): List<Song> {
        return topPlayedRepository.topTracks()
    }
}