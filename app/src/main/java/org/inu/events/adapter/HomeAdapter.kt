package org.inu.events.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.notify
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.LikeRepository
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.viewmodel.HomeViewModel
import org.inu.events.viewmodel.LikeViewModel
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(val viewModel: HomeViewModel) : ListAdapter<Event, HomeAdapter.ViewHolder>(HomeEventDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent, this, viewModel)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }

    class ViewHolder private constructor(val binding: HomeRecyclerviewItemBinding, val adapter: ListAdapter<Event, ViewHolder>, val viewModel: HomeViewModel) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        companion object{
            fun from(parent: ViewGroup, adapter: ListAdapter<Event, ViewHolder>, viewModel: HomeViewModel) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeRecyclerviewItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, adapter, viewModel)
            }
        }

        private var checkDeadline: Boolean = false

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: Event, position: Int) {
            binding.item = homeData
            binding.viewModel = viewModel
            binding.boardDate.text = whenDay(homeData.endAt)
            binding.boardDate.backgroundTintList = when(checkDeadline){
                true -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.black8))
                else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary))
            }

            binding.likeImageView.setOnClickListener{
                viewModel.onLikeClickEvent(homeData.likedByMe ?: false, homeData.id)
                if(viewModel.onLikePost()) homeData.likedByMe = !(homeData.likedByMe ?: false)
                adapter.notifyItemChanged(position)
            }
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            with(binding.root.context) {
                startActivity(DetailActivity.callingIntent(this, binding.item!!.id,binding.item!!.wroteByMe))
            }
        }

        private fun whenDay(end_at: String?): String {
            if (end_at == null) return "D-??"

            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val endDate = dateFormat.parse(end_at).time
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time.time

            var dDay = (endDate - today) / (24 * 60 * 60 * 1000)

            if (dDay < 0) {
                checkDeadline = true
                return "마감"
            }
            return "D-${if(dDay.toInt() == 0) "day" else dDay}"
        }
    }
}

class HomeEventDiffUtil : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }
}