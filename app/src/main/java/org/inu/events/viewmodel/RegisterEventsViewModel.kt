package org.inu.events.viewmodel

import android.util.Log
import androidx.databinding.*
import org.inu.events.data.RegisterEventData

//ViewModel()
class RegisterEventsViewModel : BaseObservable() {
    var registerEventData = RegisterEventData()
        set(registerEventData) {
            field = registerEventData
            notifyChange()
        }

    //val title2 = MutableLiveData("기본값")

    @get:Bindable
    val title: String
        get() = registerEventData.title
    val organization: String
        get() = registerEventData.organization
    var selectedItemPosition: Int
        get() = registerEventData.selectedItemPosition
        set(value){
            registerEventData = registerEventData.copy(selectedItemPosition = value)
        }
    val period: String
        get() = registerEventData.period
    val target: String
        get() = registerEventData.target
    val content: String
        get() = registerEventData.content

    // TODO: 수연 - 이후 갤러리에서 이미지 받아올 때 구현
    val imgResId: Int
        get() = registerEventData.imgResId

    val phase: Int
        get() = registerEventData.phase




    // TODO: 수연 - 이후 나머지 click 함수 구현
    fun onCancelClick() {
    }

    fun onNextClick() {
        Log.i("BUTTON", "다음 버튼 클릭")
        registerEventData = registerEventData.copy(phase = 2)
    }

    fun onBeforeClick() {
        Log.i("BUTTON","이전 버튼 클릭")
        registerEventData = registerEventData.copy(phase = 1)
    }

    fun onCompleteClick() {
    }



}