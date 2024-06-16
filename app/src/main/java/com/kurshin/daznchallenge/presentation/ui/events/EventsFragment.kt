package com.kurshin.daznchallenge.presentation.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kurshin.daznchallenge.data.EventRepository
import com.kurshin.daznchallenge.data.remote.RetrofitInstance
import com.kurshin.daznchallenge.databinding.FragmentEventsBinding
import com.kurshin.daznchallenge.domain.model.sortByDate
import com.kurshin.daznchallenge.presentation.adapter.EventListAdapter
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventListAdapter
    private val viewModel: EventsViewModel by viewModels {
        val repository = EventRepository(RetrofitInstance.apiService)
        EventsViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        binding.list.layoutManager = LinearLayoutManager(context)
        adapter = EventListAdapter {
            it.videoUrl?.let { videoUrl ->
                val action = EventsFragmentDirections.actionEventsFragmentToVideoFragment(videoUrl)
                findNavController().navigate(action)
            }
        }
        binding.list.adapter = adapter

        setUpEventData()
        return binding.root
    }


    private fun setUpEventData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.error.collect { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.events.collect { events ->
                        if (events.isNotEmpty()) {
                            binding.progressBar.isVisible = false
                        }
                        adapter.submitList(events.sortByDate())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}