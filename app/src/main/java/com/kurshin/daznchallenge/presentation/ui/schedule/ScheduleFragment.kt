package com.kurshin.daznchallenge.presentation.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kurshin.daznchallenge.R
import com.kurshin.daznchallenge.data.EventRepository
import com.kurshin.daznchallenge.data.remote.RetrofitInstance
import com.kurshin.daznchallenge.databinding.FragmentScheduleBinding
import com.kurshin.daznchallenge.domain.model.sortByDate
import com.kurshin.daznchallenge.domain.model.toDaznDate
import com.kurshin.daznchallenge.presentation.adapter.EventListAdapter
import com.kurshin.daznchallenge.presentation.ui.schedule.observer.ScheduleEventsObserver
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventListAdapter
    private val viewModel: ScheduleViewModel by viewModels {
        val repository = EventRepository(RetrofitInstance.apiService)
        ScheduleViewModelFactory(repository)
    }

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        adapter = EventListAdapter {
            Toast.makeText(
                requireContext(),
                getString(
                    R.string.future_event,
                    it.date
                        .toDaznDate()
                        .lowercase()
                        .replace(",", " at")
                ),
                Toast.LENGTH_LONG
            ).show()
        }
        binding.list.adapter = adapter

        setUpScheduleData()
        return binding.root
    }

    private fun setUpScheduleData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.events.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.progressBar.isVisible = false
            }
            adapter.submitList(it.sortByDate())
        }

        ScheduleEventsObserver(viewModel, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}