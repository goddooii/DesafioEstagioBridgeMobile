package com.bridge.audino.viewmodel

import android.util.Log
import com.bridge.audino.model.Track
import com.bridge.audino.repository.TrackRepository

class FavoriteTrackListViewModel : TrackListViewModel() {

    override fun onFavoriteAdded(favList: List<Track>) {
        updateFavoriteList(favList)
    }

    override fun onFavoriteRemoved(favList: List<Track>) {
        updateFavoriteList(favList)
    }

    private fun updateFavoriteList(favList: List<Track>) {
        favList.forEach {
            it.favorite = true
        }
        mTrackList.postValue(favList)
    }

    fun init() {
        TrackRepository.getFavorites({ favList ->
            updateFavoriteList(favList)
        }, {
            Log.d("TrackListViewModel", "A API falhou em resgatar a lista de musicas favoritas")
        })
    }
}
