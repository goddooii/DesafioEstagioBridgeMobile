package com.bridge.audino.model

import com.google.gson.annotations.SerializedName

data class Album(
    val id: String,
    @SerializedName("image")
    val imageUrl: String,
    val name: String
)