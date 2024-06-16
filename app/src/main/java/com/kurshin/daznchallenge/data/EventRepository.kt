package com.kurshin.daznchallenge.data

import com.kurshin.daznchallenge.data.remote.DaznService
import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.repository.IEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class EventRepository(private val apiService: DaznService) : IEventRepository {

    override fun getAllEvents(): Flow<List<DaznEvent>> = flow {
        val data = withContext(Dispatchers.IO) {
            apiService.fetchAllEvents()
        }
        emit(data)
    }

    override suspend fun getSchedule(): List<DaznEvent> {
        return withContext(Dispatchers.IO) {
            apiService.fetchSchedule()
        }
    }
}