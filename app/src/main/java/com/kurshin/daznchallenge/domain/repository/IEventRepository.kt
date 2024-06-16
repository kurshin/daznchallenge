package com.kurshin.daznchallenge.domain.repository

import com.kurshin.daznchallenge.domain.model.DaznEvent
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    fun getAllEvents(): Flow<List<DaznEvent>>
    suspend fun getSchedule(): List<DaznEvent>
}