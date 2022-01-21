package org.inu.events.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import org.inu.events.DetailActivity
import org.inu.events.data.model.Article
import org.inu.events.databinding.HomeRecyclerviewItemBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO


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
        init{
            itemView.setOnClickListener(this)
        }
        fun bind(homeData: Article) {
            binding.homeData = homeData
        }

        override fun onClick(v: View) {
            //TODO - 홈에서 클릭한 게시글이 무엇인지
            Intent(binding.root.context, DetailActivity::class.java).apply {
                putExtra(HOME_BOARD_INFO,homeDataList.value!![position].id)
            }.run{binding.root.context.startActivity(this)}
        }
    }
}
