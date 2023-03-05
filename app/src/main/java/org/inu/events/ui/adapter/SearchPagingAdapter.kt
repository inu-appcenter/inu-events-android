package org.inu.events.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemSearchEventBinding

class SearchPagingAdapter(
    val onClickEvent: (event: Event) -> Unit,
    val onCLickLikeIcon: (event: Event) -> Boolean
) :
    PagingDataAdapter<Event, SearchPagingAdapter.ViewHolder>(SearchDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(
        parent, onClickEvent, onCLickLikeIcon
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { event ->
            holder.bind(event)
        }
    }

    class ViewHolder private constructor(
        val binding: ItemSearchEventBinding,
        val _onClickEvent: (event: Event) -> Unit,
        val _onCLickLikeIcon: (event: Event) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {


        init {
            onClickEvent()
            onClickLikeIcon()
        }

        private fun onClickEvent() {
            itemView.setOnClickListener {
                binding.item?.let(_onClickEvent)
            }
        }

        private fun onClickLikeIcon() {
            binding.bookmarkIcon.setOnClickListener {
                binding.item?.let(_onCLickLikeIcon)
            }
        }

        fun bind(item: Event) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClickEvent: (event: Event) -> Unit,
                onCLickLikeIcon: (event: Event) -> Boolean
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, onClickEvent, onCLickLikeIcon)
            }
        }
    }

    companion object SearchDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
    }
}
