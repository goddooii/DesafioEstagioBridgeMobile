package com.bridge.audino.model

import com.google.gson.annotations.SerializedName

data class FullAlbum(
    val id: String,
    @SerializedName("image")
    val imageUrl: String,
    val name: String,
    val tracks: List<Track>
)