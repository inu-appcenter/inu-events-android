package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.R
import org.inu.events.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var list: List<Category>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list!![position])
    }
    override fun getItemCount() = list?.size ?: 0

    fun submitList(list: List<Category>) {
        this.list = list
    }

    class ViewHolder private constructor(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

        fun bind(item: Category) {
            binding.item = item
            val drawable = ContextCompat.getDrawable(binding.root.context, item.imageSrc)
            binding.card.setImageDrawable(drawable)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                item.isChecked = !item.isChecked
                if(item.isChecked)
                    it.background = ContextCompat.getDrawable(it.context, R.drawable.category_background_selected)
                else
                    it.background = ContextCompat.getDrawable(it.context, R.drawable.category_background)
            }
        }
    }
}

data class Category(
    val name: String,
    var isChecked: Boolean,
    val imageSrc: Int
)