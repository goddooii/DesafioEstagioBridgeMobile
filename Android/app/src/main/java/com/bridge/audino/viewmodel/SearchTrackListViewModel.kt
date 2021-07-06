package com.bridge.audino.viewmodel

import android.util.Log
import com.bridge.audino.repository.TrackRepository
import java.util.*

class SearchTrackListViewModel : TrackListViewModel() {
    private val mTimer by lazy { Timer() }
    private var mTimerTask: TimerTask? = null
    private val mTimerDelay = 1000L

    fun onQueryChanged(text: String) {
        if (mTimerTask != null) mTimerTask!!.cancel()
        mTimer.purge()
        mTimerTask = object : TimerTask() {
            override fun run() {
                TrackRepository.queryList(text, { trackList ->
                    TrackRepository.getFavorites({ favList -> updateFavoritesOnList(trackList, favList) }, {
                        Log.d("TrackListViewModel", "A API falhou em resgatar a lista de musicas favoritas")
                    })
                }) {
                    Log.d("TrackListViewModel", "A API falhou em resgatar uma lista com a query $text")
                }
            }
        }
        mTimer.schedule(mTimerTask, mTimerDelay)
    }
}