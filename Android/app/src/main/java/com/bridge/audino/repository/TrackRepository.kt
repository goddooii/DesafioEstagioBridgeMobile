package com.bridge.audino.repository

import com.bridge.audino.framework.ApiService
import com.bridge.audino.framework.RetrofitClient
import com.bridge.audino.model.FullAlbum
import com.bridge.audino.model.FullArtist
import com.bridge.audino.model.Track
import com.bridge.audino.utils.CallbackUtils

object TrackRepository {

    private val mApiService: ApiService by lazy {
        RetrofitClient.getRetrofitInstance().create(ApiService::class.java)
    }

    fun getFavorites(
        onResponse: (List<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveCallback(
            mApiService.getFavorites(),
            { favList -> onResponse(favList) },
            onFailure
        )
    }

    fun getPopular(
        onResponse: (List<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveCallback(
            mApiService.getTopList(),
            { trackList -> onResponse(trackList) },
            onFailure
        )
    }

    fun addFavorite(
        id: String,
        onResponse: (List<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveCallback(
            mApiService.addFavorite(id),
            onResponse,
            onFailure
        )
    }

    fun removeFavorite(
        id: String,
        onResponse: (List<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveCallback(
            mApiService.removeFavorite(id),
            onResponse,
            onFailure
        )
    }

    fun queryList(
        query: String,
        onResponse: (List<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveCallback(
            mApiService.queryList(query),
            onResponse,
            onFailure
        )
    }

    fun getArtist(
        artistId: String,
        onResponse: (FullArtist) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveArtistCallback(
            mApiService.getArtist(artistId),
            onResponse,
            onFailure
        )
    }

    fun getAlbum(
        albumId: String,
        onResponse: (FullAlbum) -> Unit,
        onFailure: () -> Unit
    ) {
        CallbackUtils.resolveAlbumCallback(
            mApiService.getAlbum(albumId),
            onResponse,
            onFailure
        )
    }

}