package com.bridge.audino.utils.listener

interface MusicPlayerListener {
    fun onShuffleClicked()
    fun onSkipPreviousClicked()
    fun onPlayOrPauseClicked()
    fun onSkipNextClicked()
    fun onRepeatClicked()
}