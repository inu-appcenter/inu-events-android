package org.inu.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import org.inu.events.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var homeModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)  //데이터 바인딩

        homeModel = HomeViewModel()     //뷰모델에서 데이터 가져옴
        binding.homeRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)   //리사이클러뷰 매니저 설정
            adapter = HomeAdapter(homeModel.homeDataList)   //데이터를 아답터에 전달
        }

    }
}