package com.mp3.player.retromusic.model.smartplaylist

import androidx.annotation.DrawableRes
import com.mp3.player.retromusic.R
import com.mp3.player.retromusic.model.AbsCustomPlaylist

abstract class AbsSmartPlaylist(
    name: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_queue_music
) : AbsCustomPlaylist(
    id = PlaylistIdGenerator(name, iconRes),
    name = name
)