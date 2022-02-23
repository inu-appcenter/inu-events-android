package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ItemFavoriteEventBinding

class LikeAdapter: RecyclerView.Adapter<LikeAdapter.ViewHolder>() {
    private var list: List<Event>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list!![position])
    }
    override fun getItemCount() = list?.size ?: 0

    fun submitList(list: List<Event>) {
        this.list = list
    }

    class ViewHolder private constructor(val binding: ItemFavoriteEventBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFavoriteEventBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        fun bind(item: Event) {
            binding.item = item
        }
    }
}