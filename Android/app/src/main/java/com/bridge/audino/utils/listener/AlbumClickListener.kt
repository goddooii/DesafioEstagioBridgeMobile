package com.bridge.audino.utils.listener

import com.bridge.audino.model.FullAlbum

interface AlbumClickListener {
    fun onAlbumClicked(album: FullAlbum)
}