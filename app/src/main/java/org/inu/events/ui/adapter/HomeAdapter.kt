package org.inu.events.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.ui.detail.DetailActivity
import org.inu.events.R
import org.inu.events.common.util.Period
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.ui.home.HomeViewModel

class HomeAdapter(val viewModel: HomeViewModel) :
    PagingDataAdapter<Event, HomeAdapter.ViewHolder>(HomeEventDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent, this, viewModel)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    class ViewHolder private constructor(
        val binding: HomeRecyclerviewItemBinding,
        val adapter: HomeAdapter,
        val viewModel: HomeViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        val period = Period()

        companion object {
            fun from(
                parent: ViewGroup,
                adapter: HomeAdapter,
                viewModel: HomeViewModel
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeRecyclerviewItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, adapter, viewModel)
            }
        }

        init {
            itemView.setOnClickListener {
                it.context.startActivity(
                    DetailActivity.callingIntent(
                        it.context,
                        binding.item!!.id,
                        binding.item!!.wroteByMe
                    )
                )
            }

            onClickLike()
        }

        private fun onClickLike() {
            binding.likeImageView.setOnClickListener {
                binding.item?.let { event ->
                    if (viewModel.onLikePost()) event.likedByMe = !(event.likedByMe ?: false)
                    else viewModel.onLogIn()

                    adapter.notifyItemChanged(bindingAdapterPosition)
                }
            }
        }

        fun bind(homeData: Event) {
            viewModel.eventIndex = homeData.id
            binding.item = homeData
            binding.viewModel = viewModel
            binding.boardDate.text = period.whenDay(homeData.endAt)
            binding.boardDate.backgroundTintList = when (period.checkDeadline) {
                true -> ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black8
                    )
                )
                else -> ColorStateList.valueOf(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.primary
                    )
                )
            }
            binding.executePendingBindings()
        }
    }

    companion object HomeEventDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event) =
            newItem.isSameListContent(oldItem)
    }
}