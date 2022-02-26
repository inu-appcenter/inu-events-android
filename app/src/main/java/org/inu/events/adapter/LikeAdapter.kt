package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemLikeEventBinding
import org.inu.events.viewmodel.LikeViewModel

class LikeAdapter(val viewModel: LikeViewModel) : ListAdapter<Event, LikeAdapter.ViewHolder>(LikeDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent, viewModel)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ItemLikeEventBinding, val viewModel: LikeViewModel) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewModel: LikeViewModel) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLikeEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, viewModel)
            }
        }

        fun bind(item: Event) {
            binding.item = item
            binding.viewModel = viewModel
            binding.bookmarkIcon.setOnClickListener {
                viewModel.onClickLikeIcon(item.id)
            }
            binding.cardWrap.setOnClickListener {
                viewModel.onClickDetail(it, item)
            }
            binding
            binding.executePendingBindings()
        }
    }
}

class LikeDiffUtil : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}