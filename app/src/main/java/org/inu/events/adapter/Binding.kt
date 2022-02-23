package org.inu.events.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event

@BindingAdapter("app:likes")
fun setLikes(recyclerView: RecyclerView, list: List<Event>) {
    (recyclerView.adapter as LikeAdapter).submitList(list)
}