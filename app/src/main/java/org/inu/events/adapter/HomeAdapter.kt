package org.inu.events.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors.getColor
import org.inu.events.DetailActivity
import org.inu.events.MyApplication.Companion.bindImageFromUrl
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    var homeDataList: List<Event> = listOf()
        set(v) {
            field = v
            notifyDataSetChanged()  //뷰 바뀜 리사이클러뷰한테 알려줌
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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        
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
                startActivity(DetailActivity.callingIntent(this, binding.homeData!!.id,binding.homeData!!.wroteByMe))
            }
        }

        private fun whenDay(end_at: String?): String {
            if (end_at == null) return "D-??"

            val endDate = LocalDateTime.parse(end_at, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate()
            val today = LocalDate.now()

            val dDay = Period.between(today, endDate).days

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
