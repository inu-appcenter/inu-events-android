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
    println("아악!!")
    list?.let {
        (recyclerView.adapter as CategoryAdapter).submitList(list)
    }
}