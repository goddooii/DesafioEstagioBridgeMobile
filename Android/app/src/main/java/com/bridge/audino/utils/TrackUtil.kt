package com.bridge.audino.utils

import com.bridge.audino.model.FullAlbum
import com.bridge.audino.model.FullArtist
import com.bridge.audino.model.Track

object TrackUtil {

    fun isTrackEnabled(track: Track): Boolean {
        return !track.previewUrl.isNullOrEmpty()
    }

    fun isAlbumEnabled(album: FullAlbum): Boolean {
        album.tracks.forEach {
            if (isTrackEnabled(it)) return true
        }
        return false
    }

    fun isArtistEnabled(artist: FullArtist): Boolean {
        artist.topTracks.forEach {
            if (isTrackEnabled(it)) return true
        }
        return false
    }

}