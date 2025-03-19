package com.mp3.player.retromusic.extensions

import androidx.core.view.WindowInsetsCompat
import com.mp3.player.retromusic.util.PreferenceUtil
import com.mp3.player.retromusic.util.RetroUtil

fun WindowInsetsCompat?.getBottomInsets(): Int {
    return if (PreferenceUtil.isFullScreenMode) {
        return 0
    } else {
        this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: RetroUtil.navigationBarHeight
    }
}
