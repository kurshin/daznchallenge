package com.kurshin.daznchallenge.data.remote

import com.kurshin.daznchallenge.domain.model.DaznEvent
import retrofit2.http.GET

interface DaznService {

    @GET("getEvents")
    suspend fun fetchAllEvents(): List<DaznEvent>

    @GET("getSchedule")
    suspend fun fetchSchedule(): List<DaznEvent>
}