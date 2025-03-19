package com.mp3.player.retromusic.interfaces

import com.mp3.player.retromusic.model.Album
import com.mp3.player.retromusic.model.Artist
import com.mp3.player.retromusic.model.Genre

interface IHomeClickListener {
    fun onAlbumClick(album: Album)

    fun onArtistClick(artist: Artist)

    fun onGenreClick(genre: Genre)
}