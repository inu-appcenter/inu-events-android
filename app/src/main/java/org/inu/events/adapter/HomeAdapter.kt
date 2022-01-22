package org.inu.events.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.R
import org.inu.events.data.model.Article
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO
import org.inu.events.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*


class HomeAdapter(private var homeDataList: LiveData<List<Article>>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(homeDataList.value!![position])
    }

    override fun getItemCount() = homeDataList.value?.size ?: 0

    inner class HomeViewHolder(private val binding: HomeRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        var checkDeadline:Boolean = false
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: Article) {
            binding.homeData = homeData
            binding.boardDate.text = whenDay(homeData.end_at)
            binding.boardDate.background = ContextCompat.getDrawable(binding.root.context,isDeadline())
            Log.d("tag","homeData bind")
        }

        override fun onClick(v: View) {
            Intent(binding.root.context, DetailActivity::class.java).apply {
                putExtra(HOME_BOARD_INFO,homeDataList.value!![position].id)
            }.run{binding.root.context.startActivity(this)}
        }

        private fun whenDay(end_at: String?): String {
            if(end_at == null) return "D-??"

            val dateFormat = SimpleDateFormat("yyyy.MM.dd")

            val endDate = dateFormat.parse(end_at).time
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time.time

            var dDay = (endDate-today) / (24*60*60*1000)
            Log.d("tag","D-$dDay")

            if(dDay < 0){
                checkDeadline = true
                return "마감"
            }
            return "D-$dDay"
        }

        private fun isDeadline(): Int = if (checkDeadline) {R.drawable.drawable_home_board_date_deadline_background}
                                else {R.drawable.drawable_home_board_date_ongoing_background}
    }
}
