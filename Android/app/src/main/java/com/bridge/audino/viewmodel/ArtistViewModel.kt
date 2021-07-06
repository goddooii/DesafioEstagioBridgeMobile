package com.bridge.audino.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bridge.audino.model.FullArtist
import com.bridge.audino.repository.TrackRepository

class ArtistViewModel : TrackListViewModel() {
    companion object {
        const val ARTIST_ID = "com.bridge.audino.viewmodel.ArtistActivity.artistId"
    }

    private val mFullArtist: MutableLiveData<FullArtist> by lazy { MutableLiveData<FullArtist>() }

    fun getFullArtist(): LiveData<FullArtist> {
        return mFullArtist
    }

    fun init(artistId: String?) {
        if (artistId == null) throw Exception("Não foi passado o id do artista")
        TrackRepository.getArtist(artistId, { fullArtist ->
            mFullArtist.postValue(fullArtist)
            TrackRepository.getFavorites({ favList -> updateFavoritesOnList(fullArtist.topTracks, favList) }, {
                Log.d("TrackListViewModel", "A API falhou em resgatar a lista de musicas favoritas")
            })
        }) {
            Log.d("TrackListViewModel", "A API falhou em resgatar o artista de id $artistId")
        }
    }


}