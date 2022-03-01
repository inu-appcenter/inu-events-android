package org.inu.events.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter : ListAdapter<Event, HomeAdapter.ViewHolder>(HomeEventDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent, this)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: HomeRecyclerviewItemBinding, val adapter: ListAdapter<Event, ViewHolder>) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        companion object{
            fun from(parent: ViewGroup, adapter: ListAdapter<Event, ViewHolder>) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HomeRecyclerviewItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, adapter)
            }
        }

        private var checkDeadline: Boolean = false

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: Event) {
            binding.item = homeData
            binding.boardDate.text = whenDay(homeData.endAt)
            binding.boardDate.backgroundTintList = when(checkDeadline){
                true -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.black8))
                else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary))
            }

            binding.likeImageView.setOnClickListener{

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