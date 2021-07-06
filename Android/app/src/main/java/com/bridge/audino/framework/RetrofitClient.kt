package com.bridge.audino.framework

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://caio-musicplayer.builtwithdark.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}