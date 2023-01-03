package org.inu.events.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.data.model.entity.User
import org.inu.events.databinding.ItemBlockedAccountBinding
import org.inu.events.ui.util.dialog.TwoButtonDialog
import org.inu.events.ui.mypage.shortcut.BlockedAccountViewModel

class BlockedAccountAdapter(val viewModel: BlockedAccountViewModel) :
    ListAdapter<User, BlockedAccountAdapter.ViewHolder>(BlockedAccountDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        val binding: ItemBlockedAccountBinding,
        val viewModel: BlockedAccountViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cancelBlockingButton.setOnClickListener {
                binding.item?.let { user ->
                    TwoButtonDialog("차단을 해제하시겠어요?") {
                        viewModel.onClickCancelBlocking(user.id!!)
                    }.show(it.context)
                }
            }
        }

        fun bind(item: User) {
            binding.item = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: BlockedAccountViewModel): ViewHolder {
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