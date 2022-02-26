package org.inu.events.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.R
import org.inu.events.databinding.ItemCategoryBinding

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(CategoriesDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent, this)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class ViewHolder private constructor(val binding: ItemCategoryBinding, val adapter: ListAdapter<Category, ViewHolder>) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, adapter: ListAdapter<Category, ViewHolder>) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, adapter)
            }
        }

        fun bind(item: Category, position: Int) {
            binding.item = item
            val drawable = ContextCompat.getDrawable(binding.root.context, item.imageSrc)
            binding.card.setImageDrawable(drawable)

            binding.root.setOnClickListener {
                item.isChecked = !item.isChecked
                adapter.notifyItemChanged(position)
            }

            if(item.isChecked)
                binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.category_background_selected)
            else
                binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.category_background)

            binding.executePendingBindings()
        }
    }
}

data class Category(
    val name: String,
    var isChecked: Boolean,
    val imageSrc: Int
)

class CategoriesDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        Log.e("참조확인", "${oldItem.isChecked}, ${newItem.isChecked}")
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        Log.e("으잉??", "${oldItem.isChecked}, ${newItem.isChecked}")
        return oldItem.isChecked == newItem.isChecked
    }
}