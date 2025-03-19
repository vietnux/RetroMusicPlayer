package com.mp3.player.retromusic.interfaces

import android.view.View
import com.mp3.player.retromusic.model.Genre

interface IGenreClickListener {
    fun onClickGenre(genre: Genre, view: View)
}