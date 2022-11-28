package org.inu.events.adapter

import android.content.res.ColorStateList
import android.util.Log
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
import org.inu.events.common.util.Period
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.LikeRepository
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.viewmodel.HomeViewModel
import org.inu.events.viewmodel.LikeViewModel
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(var viewModel: HomeViewModel) : ListAdapter<Event, HomeAdapter.ViewHolder>(HomeEventDiffUtil()){

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

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(homeData: Event, position: Int) {
            val period = Period()
            Log.d("asdfasdfasdf", homeData.title)
            binding.item = homeData
            binding.viewModel = viewModel
            viewModel.eventIndex = homeData.id
            binding.boardDate.text = period.whenDay(homeData.endAt)
            binding.boardDate.backgroundTintList = when(period.checkDeadline){
                true -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.black8))
                else -> ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary))
            }

            binding.likeImageView.setOnClickListener{
                if(viewModel.onLikePost()) homeData.likedByMe = !(homeData.likedByMe ?: false)
                else viewModel.onLogIn()
                adapter.notifyItemChanged(position)
            }
            binding.executePendingBindings()
        }

        override fun onClick(v: View) {
            with(binding.root.context) {
                startActivity(DetailActivity.callingIntent(this, binding.item!!.id,binding.item!!.wroteByMe))
            }
        }

//        fun replaceList()
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