package com.kurshin.daznchallenge.presentation.ui.schedule.observer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kurshin.daznchallenge.presentation.ui.schedule.ScheduleViewModel
import kotlinx.coroutines.Job

class ScheduleEventsObserver(
    private val viewModel: ScheduleViewModel,
    lifecycleOwner: LifecycleOwner
) : DefaultLifecycleObserver {

    private var job: Job? = null

    override fun onResume(owner: LifecycleOwner) {
        job = viewModel.fetchScheduleEvents()
    }

    override fun onPause(owner: LifecycleOwner) {
        job?.cancel()
        job = null
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
}