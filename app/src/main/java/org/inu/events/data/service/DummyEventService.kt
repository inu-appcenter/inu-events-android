package org.inu.events.data.service

import org.inu.events.R
import org.inu.events.data.HomeData

class DummyEventService : EventService {
    override fun fetchEvent(): List<HomeData> {
        return listOf(
            HomeData("뭐라구?앱센터 13기 신입 멤버를 모집한다구?","마감","동아리",
                R.drawable.img_home_board_sample_image1,
                R.drawable.drawable_home_board_date_deadline_background
            ),
            HomeData("뭐라구?앱센터 13기 신입 멤버를 모집한다구?","D-12","동아리",
                R.drawable.img_home_board_sample_image2,
                R.drawable.drawable_home_board_date_deadline_background
            ),
            HomeData("배고파요","마감","동아리",
                R.drawable.img_profile,
                R.drawable.drawable_home_board_date_ongoing_background
            ),
            HomeData("앱센터 13기 모집","D-12","동아리",
                R.drawable.img_home_board_sample_image1,
                R.drawable.drawable_home_board_date_deadline_background
            )
        )
    }
}