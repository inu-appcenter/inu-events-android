package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.data.HomeData
import org.inu.events.util.SingleLiveEvent

class HomeViewModel : ViewModel() {
    private val _homeDataList = MutableLiveData<ArrayList<HomeData>>()
    val homeDataList : LiveData<ArrayList<HomeData>>
        get() = _homeDataList

    private var homeData = ArrayList<HomeData>()

    val postClickEvent = SingleLiveEvent<Any>()

    init{
        homeData = loadHomeData()
        _homeDataList.value = homeData
    }


    //TODO("서버나오면 수정해야 할 부분")
    //일단은 데이터가 없으니까 임시로 넣어줌
    private fun loadHomeData(): ArrayList<HomeData> {
        val homeDataList = ArrayList<HomeData>()
        homeDataList.add(HomeData("뭐라구?앱센터 13기 신입 멤버를 모집한다구?","마감","동아리",
            R.drawable.img_home_board_sample_image1,
            R.drawable.drawable_home_board_date_deadline_background
        ))
        homeDataList.add(HomeData("앱센터 13기 모집","D-12","동아리",
            R.drawable.img_home_board_sample_image2,
            R.drawable.drawable_home_board_date_ongoing_background
        ))
        homeDataList.add(HomeData("뭐라구?앱센터가 문제가 아니라 제목이 길어서 문제라고?","D-13","동아리",
            R.drawable.img_home_board_sample_image1,
            R.drawable.drawable_home_board_date_ongoing_background
        ))
        homeDataList.add(HomeData("배고파요","D-17","수달",
            R.drawable.img_profile,
            R.drawable.drawable_home_board_date_ongoing_background
        ))
        return homeDataList
    }

    fun onClickPost() {
        postClickEvent.call()
    }
}