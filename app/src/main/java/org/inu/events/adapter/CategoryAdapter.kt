package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var list: List<Category>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("빼애액!!")
        holder.bind(list!![position])
    }
    override fun getItemCount() = list?.size ?: 0

    fun submitList(list: List<Category>) {
        println("빼애액!!!")
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
            binding.executePendingBindings()
        }
    }
}

data class Category(
    val name: String,
    val isChecked: Boolean,
    val imageSrc: Int
)