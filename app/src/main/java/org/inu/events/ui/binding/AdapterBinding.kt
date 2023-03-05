package org.inu.events.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.Event
import org.inu.events.data.model.entity.User
import org.inu.events.ui.adapter.*
import org.inu.events.ui.adapter.LikeAdapter

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

@BindingAdapter("app:blockedAccounts")
fun setBlockedAccounts(recyclerView: RecyclerView, list: List<User>?) {
    list?.let {
        (recyclerView.adapter as BlockedAccountAdapter).submitList(list)
    }
}