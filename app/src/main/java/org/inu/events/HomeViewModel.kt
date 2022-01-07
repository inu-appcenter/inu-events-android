package org.inu.events

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class HomeViewModel : BaseObservable() {
    var homeData: HomeData? = null
        set(homeData){
            field = homeData
            notifyChange()
        }

    @get:Bindable

    val title: String?
        get() = homeData?.title
    val date: String?
        get() = homeData?.date
    val institution: String?
        get() = homeData?.institution
    val imageResId: Int?
        get() = homeData?.imageResId
    val dateColor: Int?
        get() = homeData?.dateColor

    val homeDataList: List<HomeData>

    init{
        homeDataList = loadHomeData()
    }

    //TODO("서버나오면 수정해야 할 부분")
    //일단은 데이터가 없으니까 임시로 넣어줌
    private fun loadHomeData(): List<HomeData>{
        val homeDataList = mutableListOf<HomeData>()
        homeDataList.add(HomeData("뭐라구?앱센터 13기 신입 멤버를 모집한다구?","마감","동아리",R.drawable.home_board_sample_image1,R.drawable.home_board_date_deadline_background))
        homeDataList.add(HomeData("앱센터 13기 모집","D-12","동아리",R.drawable.home_board_sample_image2,R.drawable.home_board_date_ongoing_background))
        homeDataList.add(HomeData("뭐라구?앱센터가 문제가 아니라 제목이 길어서 문제라고?","D-13","동아리",R.drawable.home_board_sample_image1,R.drawable.home_board_date_ongoing_background))
        return homeDataList
    }
}