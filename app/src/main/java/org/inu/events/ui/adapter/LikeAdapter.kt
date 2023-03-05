package org.inu.events.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemLikeEventBinding
import org.inu.events.ui.mypage.store.LikeViewModel

class LikeAdapter(val viewModel: LikeViewModel) :
    ListAdapter<Event, LikeAdapter.ViewHolder>(LikeDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent, viewModel)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        val binding: ItemLikeEventBinding,
        val viewModel: LikeViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewModel: LikeViewModel): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLikeEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, viewModel)
            }
        }

        init {
            binding.bookmarkIcon.setOnClickListener {
                binding.item?.let { event ->
                    viewModel.onClickLikeIcon(event.id, it)
                }
            }

            binding.cardWrap.setOnClickListener {
                binding.item?.let { event ->
                    viewModel.onClickDetail(it, event)
                }
            }
        }

        fun bind(item: Event) {
            binding.item = item
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    companion object LikeDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}

