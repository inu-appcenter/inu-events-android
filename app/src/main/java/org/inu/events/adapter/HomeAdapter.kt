package org.inu.events.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO
import java.text.SimpleDateFormat
import java.util.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var homeDataList: List<Event> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            HomeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(homeDataList[position])
    }

    override fun getItemCount() = homeDataList.size ?: 0

    inner class HomeViewHolder(private val binding: HomeRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var checkDeadline: Boolean = false

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(homeData: Event) {
            binding.homeData = homeData
            binding.boardDate.text = whenDay(homeData.endAt)
            binding.boardDate.background =
                ContextCompat.getDrawable(binding.root.context, isDeadline())
        }

        override fun onClick(v: View) {
            with(binding.root.context) {
                startActivity(DetailActivity.callingIntent(this, binding.homeData!!.id))
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
            return "D-$dDay"
        }

        private fun isDeadline(): Int = if (checkDeadline) {
            R.drawable.drawable_home_board_date_deadline_background
        } else {
            R.drawable.drawable_home_board_date_ongoing_background
        }
    }
}
