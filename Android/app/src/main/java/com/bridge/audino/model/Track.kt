package com.bridge.audino.model


data class Track(

    val id: String,
    val previewUrl: String?,
    val durationSeconds: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    var favorite: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Track) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}