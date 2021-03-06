package org.inu.events.data.repository.mock

import okhttp3.MultipartBody
import org.inu.events.R
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.dto.UploadImageResult
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.koin.core.component.KoinComponent
/*
class EventRepositoryMock() : EventRepository, KoinComponent {
    private val e: ArrayList<Event> = arrayListOf(
        Event(
            0,
            717,
            "앱센터",
            "동아리",
            "뭐라구?앱센터 13기 신입 멤버를 모집한다구?",
            "위하여 물방아 작고 옷을 청춘 따뜻한 낙원을 이상의 피부가 봄바람이다. 인도하겠다는 할지니, 몸이 청춘에서만 튼튼하며, 발휘하기 천고에 끝까지 것이다. 대중을 인류의 살았으며, 같은 유소년에게서 반짝이는 보라. 용기가 그들에게 꽃 소담스러운 위하여 광야에서 능히 칼이다. 곧 생의 꾸며 수 쓸쓸하랴? 눈에 없으면 소금이라 이것은 아름다우냐? 끓는 들어 대고, 밝은 있는 있으랴? 같은 속에 꽃 곧 할지니, 생명을 사막이다. 이상을 열락의 이성은 없는 인간이 사랑의 피는 보내는 아니다. 이상의 인류의 반짝이는 능히 가진 노래하며 위하여 것이다. 있는 따뜻한 곳으로 수 끓는다.\n" +
                "\n" +
                "있음으로써 풀이 길지 오직 위하여서. 천고에 희망의 갑 싶이 같은 동력은 피고, 봄바람이다. 착목한는 관현악이며, 굳세게 힘차게 싶이 가진 철환하였는가? 사랑의 수 얼마나 길지 청춘의 꽃이 있으랴? 뜨고, 열매를 되려니와, 아름답고 가슴에 운다. 목숨이 하여도 일월과 무엇을 이성은 봄날의 것이 할지라도 천자만홍이 교향악이다. 아름답고 온갖 위하여서 수 길을 쓸쓸하랴? 시들어 속에서 원질이 것이 생의 그들에게 뜨거운지라, 온갖 것이다. 오아이스도 그것을 피가 철환하였는가? 청춘의 뛰노는 앞이 청춘의 아니다. 인간은 피고 쓸쓸한 청춘의 피가 있는가?\n" +
                "\n" +
                "용감하고 예수는 심장은 그들은 것이다. 품었기 피어나기 하였으며, 때문이다. 인생을 갑 싸인 현저하게 품에 있다. 그들은 뜨거운지라, 군영과 충분히 노래하며 끓는다. 황금시대를 길을 것은 평화스러운 얼음이 그들은 가치를 꽃이 하는 아니다. 청춘의 찾아 관현악이며, 그들을 쓸쓸하랴? 싸인 예수는 발휘하기 바이며, 때문이다. 우리의 구하기 살 그들을 위하여, 봄바람이다. 온갖 찬미를 생명을 꾸며 피어나는 것이다.",
            "https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg",
            "https://github.com/inu-appcenter",
            "2021-03-22 07:00:01",
            "2021-03-26 07:12:01",
            "2021.03.14 07:00:01",
            true,
            notificationSetByMe = null,
            notificationSetFor = null
        ),
        Event(
            1,
            123,
            "수달",
            "대회 공모전",
            "으헝헝 나는 감자",
            "수달 귀엽다. 배고프다.\n이 세상에서 제일 맛있는 음식은 닭발~!",
            R.drawable.img_profile.toString(),
            "https://github.com/inu-appcenter",
            "2001-03-14 07:10:01",
            "2001-07-17 07:17:01",
            "2001.01.01 08:00:01",
            false,
            notificationSetByMe = null,
            notificationSetFor = null
        ),
        Event(
            2,
            123,
            "컴퓨터공학부",
            "간식나눔",
            "알파카파카",
            "알파카털로 만든 파카는 알파카파카\n알파카파카를 팔고싶으면?\n\n\n알파카파카파까?",
            R.drawable.img_home_board_sample_image2.toString(),
            "https://github.com/inu-appcenter",
            "2022-01-25 18:26:01",
            "2022-02-28 20:00:01",
            "2022.01.23 07:00:01",
            true,
            notificationSetByMe = null,
            notificationSetFor = null
        )
    )

    override fun getEvents(): List<Event> {
        return e
    }

    override fun getEvent(eventId: Int): Event {
        return e.find { it.id == eventId }!!
    }

    override fun postEvent(params: AddEventParams) {
        e.add(Event(
            e.size,
            userId = 717,
            params.host,
            params.category,
            params.title,
            params.body,
            params.imageUuid,
            "https://github.com/inu-appcenter",
            params.startAt,
            params.endAt,
            createdAt = "2001-07-17 07:35:00",       //임시
            wroteByMe = true,
            notificationSetByMe = null,
            notificationSetFor = null
        ))
    }

    override fun updateEvent(id: Int, params: UpdateEventParams) {
        e[id].apply {
            category = params.category
            startAt = params.startAt
            endAt = params.endAt
            title = params.title
            body = params.body
            host = params.host
            imageUuid = params.imageUuid
        }
    }

    override fun deleteEvent(eventId: Int) {
        e.removeIf { it.id == eventId }
    }

    override fun uploadImage(image: MultipartBody.Part): UploadImageResult {
        TODO("Not yet implemented")
    }

}*/