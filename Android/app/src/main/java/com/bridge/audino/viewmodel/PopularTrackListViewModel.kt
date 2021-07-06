package com.bridge.audino.viewmodel

import android.util.Log
import com.bridge.audino.model.Track
import com.bridge.audino.repository.TrackRepository

class PopularTrackListViewModel : TrackListViewModel() {

    fun init() {
        TrackRepository.getPopular({ topList ->
            TrackRepository.getFavorites({ favList ->
                topList.forEach { it.favorite = favList.contains(it) }
                mTrackList.postValue(topList)
            }, {
                Log.d("TrackListViewModel", "A API falhou em resgatar a lista de músicas favoritas")
            })
        }, {
            Log.d("TrackListViewModel", "A API falhou em resgatar a lista de top músicas")
        })
    }
}