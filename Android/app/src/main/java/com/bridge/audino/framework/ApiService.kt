package com.bridge.audino.framework

import com.bridge.audino.model.FullAlbum
import com.bridge.audino.model.FullArtist
import com.bridge.audino.model.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val EMAIL = "seuemail@gmail.com"
    }

    @GET("/tracks/{id}")
    fun getTrack(@Path("id") id: String): Call<Track>

    @GET("/artists/{id}")
    fun getArtist(@Path("id") id: String): Call<FullArtist>

    @GET("/albums/{id}")
    fun getAlbum(@Path("id") id: String): Call<FullAlbum>

    @GET("/top")
    fun getTopList(): Call<MutableList<Track>>

    @GET("/search")
    fun queryList(@Query("q") query: String): Call<MutableList<Track>>

    @GET("/favorites")
    fun getFavorites(@Query("email") email: String = EMAIL): Call<MutableList<Track>>

    @POST("/favorites/add/{id}")
    fun addFavorite(@Path("id") id: String, @Query("email") email: String = EMAIL): Call<MutableList<Track>>

    @POST("/favorites/remove/{id}")
    fun removeFavorite(@Path("id") id: String, @Query("email") email: String = EMAIL): Call<MutableList<Track>>
}