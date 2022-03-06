package org.inu.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.User
import org.inu.events.databinding.ItemBlockedAccountBinding
import org.inu.events.viewmodel.BlockedAccountViewModel

class BlockedAccountAdapter(val viewModel: BlockedAccountViewModel) : ListAdapter<User, BlockedAccountAdapter.ViewHolder>(BlockedAccountDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ItemBlockedAccountBinding, val viewModel: BlockedAccountViewModel) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.nickname.text = item.nickname
            binding.cancelBlockingButton.setOnClickListener {
                viewModel.onClickCancelBlocking(item.id!!)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: BlockedAccountViewModel) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBlockedAccountBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, viewModel)
            }
        }
    }
}

class BlockedAccountDiffUtil : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}