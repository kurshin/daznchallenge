package com.kurshin.daznchallenge.presentation.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.repository.IEventRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal const val DELAY_TIME_MILLIS = 30 * 1000L

class ScheduleViewModel(private val repository: IEventRepository) : ViewModel() {

    private val _events = MutableLiveData<List<DaznEvent>>()
    val events: LiveData<List<DaznEvent>> = _events

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> get() = _error

    fun fetchScheduleEvents(): Job = viewModelScope.launch {
        while(isActive) {
            try {
                val allEvents = repository.getSchedule()
                _events.postValue(allEvents)
            } catch (error: Exception) {
                _error.emit(error.message ?: "Unknown error")
            }
            delay(DELAY_TIME_MILLIS)
        }
    }
}

class ScheduleViewModelFactory(
    private val repository: IEventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}