package com.bridge.audino.utils

import com.bridge.audino.model.FullAlbum
import com.bridge.audino.model.FullArtist
import com.bridge.audino.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CallbackUtils {

    fun resolveCallback(
        callback: Call<MutableList<Track>>,
        onResponse: (MutableList<Track>) -> Unit,
        onFailure: () -> Unit
    ) {
        callback.enqueue(object : Callback<MutableList<Track>> {
            override fun onResponse(
                call: Call<MutableList<Track>>,
                response: Response<MutableList<Track>>
            ) {
                val tracks = response.body()!!
                onResponse(tracks)
            }

            override fun onFailure(call: Call<MutableList<Track>>, t: Throwable) {
                onFailure()
            }
        })
    }

    fun resolveArtistCallback(
        callback: Call<FullArtist>,
        onResponse: (FullArtist) -> Unit,
        onFailure: () -> Unit
    ) {
        callback.enqueue(object : Callback<FullArtist> {
            override fun onResponse(
                call: Call<FullArtist>,
                response: Response<FullArtist>
            ) {
                val fullArtist = response.body()!!
                onResponse(fullArtist)
            }

            override fun onFailure(call: Call<FullArtist>, t: Throwable) {
                onFailure()
            }
        })
    }

    fun resolveAlbumCallback(
        callback: Call<FullAlbum>,
        onResponse: (FullAlbum) -> Unit,
        onFailure: () -> Unit
    ) {
        callback.enqueue(object : Callback<FullAlbum> {
            override fun onResponse(
                call: Call<FullAlbum>,
                response: Response<FullAlbum>
            ) {
                val fullAlbum = response.body()!!
                onResponse(fullAlbum)
            }

            override fun onFailure(call: Call<FullAlbum>, t: Throwable) {
                onFailure()
            }
        })
    }
}