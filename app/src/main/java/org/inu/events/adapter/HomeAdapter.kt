package org.inu.events.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.data.HomeData
import org.inu.events.viewmodel.HomeViewModel
import org.inu.events.R
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO


class HomeAdapter(private var homeDataList: List<HomeData>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = DataBindingUtil.inflate<HomeRecyclerviewItemBinding>(
            LayoutInflater.from(parent.context) , R.layout.home_recyclerview_item, parent ,false)   //이부분 헷갈림
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val homeData = homeDataList[position]
        holder.bind(homeData)
    }

    override fun getItemCount() = homeDataList.size

    inner class HomeViewHolder(private val binding: HomeRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        init{
            binding.viewModel = HomeViewModel()
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: HomeData) {
            binding.apply {
                viewModel?.homeData = homeData
                executePendingBindings()
            }
        }

        override fun onClick(v: View) {
            var homeBoardInfo = 0   //TODO 서버 나온 후 바꾸기
            Intent(binding.root.context, DetailActivity::class.java).apply {
                putExtra(HOME_BOARD_INFO,homeBoardInfo)
            }.run{binding.root.context.startActivity(this)}
        }
    }
}
