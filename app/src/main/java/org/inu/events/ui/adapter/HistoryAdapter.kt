package org.inu.events.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemMyHistoryBinding
import org.inu.events.ui.mypage.shortcut.MyHistoryViewModel

class HistoryAdapter(val viewModel: MyHistoryViewModel) :
    ListAdapter<Event, HistoryAdapter.ViewHolder>(NotificationEventAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent, viewModel)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        val binding: ItemMyHistoryBinding,
        val viewModel: MyHistoryViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, viewModel: MyHistoryViewModel): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMyHistoryBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, viewModel)
            }
        }

        init {
            itemView.setOnClickListener {
                binding.item?.let { event ->
                    viewModel.onClickDetail(it, event)
                }
            }
        }

        fun bind(item: Event) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}