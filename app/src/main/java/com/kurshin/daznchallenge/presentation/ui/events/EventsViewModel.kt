package com.kurshin.daznchallenge.presentation.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.repository.IEventRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EventsViewModel(private val repository: IEventRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<DaznEvent>>(emptyList())
    val events: StateFlow<List<DaznEvent>> = _events

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> get() = _error

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        runBlocking {
            _error.emit(throwable.message ?: "Unknown error")
        }
    }

    init {
        fetchEvents()
    }

    internal fun fetchEvents() = viewModelScope.launch (errorHandler) {
        repository.getAllEvents().collect {
            _events.value = it
        }
    }
}

class EventsViewModelFactory(
    private val repository: IEventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}