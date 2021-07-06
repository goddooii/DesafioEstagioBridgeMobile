package com.bridge.audino.model

import com.google.gson.annotations.SerializedName

data class FullArtist(
    val id: String,
    @SerializedName("image")
    val imageUrl: String,
    val name: String,
    val albums: List<FullAlbum>,
    @SerializedName("top_tracks")
    val topTracks: List<Track>
)