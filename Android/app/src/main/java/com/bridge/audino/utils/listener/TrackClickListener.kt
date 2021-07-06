package com.bridge.audino.utils.listener

import com.bridge.audino.model.Track

interface TrackClickListener {
    fun onTrackClicked(track: Track)
}