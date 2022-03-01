package org.inu.events.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event

@BindingAdapter("app:likes")
fun setLikes(recyclerView: RecyclerView, list: List<Event>?) {
    list?.let {
        (recyclerView.adapter as LikeAdapter).submitList(list)
    }
}

@BindingAdapter("app:categories")
fun setCategories(recyclerView: RecyclerView, list: List<Category>?) {
    list?.let {
        (recyclerView.adapter as CategoryAdapter).submitList(list)
    }
}

@BindingAdapter("app:notifications")
fun setNotificationEvents(recyclerView: RecyclerView, list: List<Event>?) {
    list?.let {
        (recyclerView.adapter as NotificationEventAdapter).submitList(list)
    }
}

@BindingAdapter("app:events")
fun setEvents(recyclerView: RecyclerView, list: List<Event>?) {
    list?.let {
        (recyclerView.adapter as HistoryAdapter).submitList(list)
    }
}

@BindingAdapter("app:home_events")
fun setHomeEvents(recyclerView: RecyclerView, list: List<Event>?) {
    list?.let {
        (recyclerView.adapter as HomeAdapter).submitList(list)
    }
}