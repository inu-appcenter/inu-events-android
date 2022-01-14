package org.inu.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.inu.events.adapter.HomeAdapter
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var homeModel: HomeViewModel

    private val imageView: ImageView by lazy {
        findViewById(R.id.imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents)
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)  //데이터 바인딩

        homeModel = ViewModelProvider(this).get(HomeViewModel::class.java)    //뷰모델에서 데이터 가져옴

        binding.homeRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)   //리사이클러뷰 매니저 설정
            adapter = HomeAdapter(homeModel.homeDataList)   //데이터를 아답터에 전달
        }

        // 임시
        clickImageView()
    }

    private fun clickImageView(){
        imageView.setOnClickListener {
            startActivity(Intent(this,RegisterEventsActivity::class.java))
        }
    }
}