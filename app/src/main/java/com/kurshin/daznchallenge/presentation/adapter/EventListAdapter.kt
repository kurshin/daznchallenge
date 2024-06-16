package com.kurshin.daznchallenge.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kurshin.daznchallenge.databinding.ItemEventBinding
import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.model.toDaznDate

class EventListAdapter(
    private val eventClickListener: (item: DaznEvent) -> Unit = {}
) : ListAdapter<DaznEvent, EventListAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(view, eventClickListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val eventClickListener: (item: DaznEvent) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: DaznEvent) {
            binding.title.text = event.title
            binding.subtitle.text = event.subtitle
            binding.time.text = event.date.toDaznDate()
            Glide.with(itemView.context)
                .load(event.imageUrl)
                .into(binding.thumbnail)

            binding.root.setOnClickListener {
                eventClickListener.invoke(event)
            }
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<DaznEvent>() {
        override fun areItemsTheSame(oldItem: DaznEvent, newItem: DaznEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DaznEvent, newItem: DaznEvent): Boolean {
            return oldItem == newItem
        }
    }
}

