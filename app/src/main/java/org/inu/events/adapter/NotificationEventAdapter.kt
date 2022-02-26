package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemNotificationEventBinding
import org.inu.events.viewmodel.NotificationViewModel

class NotificationEventAdapter(val viewModel: NotificationViewModel) : ListAdapter<Event, NotificationEventAdapter.ViewHolder>(EventDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent, viewModel)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ItemNotificationEventBinding, val viewModel: NotificationViewModel) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewModel: NotificationViewModel) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNotificationEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, viewModel)
            }
        }

        fun bind(item: Event) {
            binding.item = item
            binding.viewModel = viewModel
            binding.notificationIcon.setOnClickListener {
                viewModel.onClickNotificationIcon(item.id)
            }
            binding.executePendingBindings()
        }
    }
}

class EventDiffUtil : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}