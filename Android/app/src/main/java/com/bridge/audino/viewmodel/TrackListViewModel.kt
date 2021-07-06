package com.bridge.audino.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridge.audino.model.Track
import com.bridge.audino.repository.TrackRepository
import com.bridge.audino.utils.listener.FavoriteClickListener

abstract class TrackListViewModel : ViewModel(), FavoriteClickListener {
    protected val mTrackList: MutableLiveData<List<Track>> by lazy { MutableLiveData<List<Track>>() }

    fun getTrackList(): LiveData<List<Track>> {
        return mTrackList
    }

    override fun onFavoriteClicked(id: String) {
        val trackClicked = mTrackList.value?.find { it.id == id } ?: return
        if (trackClicked.favorite) {
            TrackRepository.removeFavorite(id, this::onFavoriteRemoved) {
                Log.d(
                    "TrackListViewModel",
                    "A API falhou em remover a musica de id $id da lista de musicas favoritas"
                )
            }
        } else {
            TrackRepository.addFavorite(id, this::onFavoriteAdded) {
                Log.d(
                    "TrackListViewModel",
                    "A API falhou em adicionar a musica de id $id à lista de musicas favoritas"
                )
            }
        }
    }

    fun updateFavoritesOnList(list: List<Track>?, favList: List<Track>) {
        val newList = list?.map {
            it.copy().apply {
                this.favorite = it in favList
            }
        }
        mTrackList.postValue(newList)
    }

    open fun onFavoriteAdded(favList: List<Track>) {
        updateFavoritesOnList(mTrackList.value, favList)
    }

    open fun onFavoriteRemoved(favList: List<Track>) {
        updateFavoritesOnList(mTrackList.value, favList)
    }
}