package com.kurshin.daznchallenge.data.remote

import com.kurshin.daznchallenge.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.HOST_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: DaznService by lazy {
        retrofit.create(DaznService::class.java)
    }
}