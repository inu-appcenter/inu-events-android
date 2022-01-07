package org.inu.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.databinding.HomeRecyclerviewItemBinding

class HomeAdapter(private var homeDataList: List<HomeData>):RecyclerView.Adapter<HomeViewHolder>() {

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
}
class HomeViewHolder(private val binding: HomeRecyclerviewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    init{
        binding.viewModel = HomeViewModel()
    }
    fun bind(homeData:HomeData){
        binding.apply {
            viewModel?.homeData = homeData
            executePendingBindings()
        }
    }
}