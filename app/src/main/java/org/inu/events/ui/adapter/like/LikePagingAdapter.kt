package org.inu.events.ui.adapter.like

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemLikeEventBinding

class LikePagingAdapter : PagingDataAdapter<Event, LikePagingAdapter.ViewHolder>(LikeDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { event ->
            holder.bind(event)
        }
    }

    class ViewHolder private constructor(
        val binding: ItemLikeEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLikeEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    companion object LikeDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
