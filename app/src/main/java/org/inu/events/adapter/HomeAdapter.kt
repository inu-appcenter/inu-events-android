package org.inu.events.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.data.HomeData
import org.inu.events.viewmodel.HomeViewModel
import org.inu.events.R
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO


class HomeAdapter(private var homeDataList: LiveData<ArrayList<HomeData>>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(homeDataList.value!!.get(position))
    }

    override fun getItemCount() = homeDataList.value!!.size

    inner class HomeViewHolder(private val binding: HomeRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: HomeData) {
            binding.homeData = homeData
        }

        override fun onClick(v: View) {
            val homeBoardInfo = 0   //TODO 서버 나온 후 바꾸기
            Intent(binding.root.context, DetailActivity::class.java).apply {
                putExtra(HOME_BOARD_INFO,homeBoardInfo)
            }.run{binding.root.context.startActivity(this)}
        }
    }
}
