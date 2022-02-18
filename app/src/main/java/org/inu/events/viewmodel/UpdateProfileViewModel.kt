package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateProfileViewModel : ViewModel() {
    val inputText = MutableLiveData<String>("맥도날드사고싶다")
    val errorMessage = MutableLiveData<String>("")

    fun validateNickname() : Boolean {
        errorMessage.value = when {
            inputText.value!!.isEmpty() -> "공백은 닉네임으로 설정하실 수 없습니다."
            isDuplicate() -> "중복된 닉네임입니다."
            !isSuitableLength() -> "닉네임은 8자 이내로 설정해 주세요."
            else -> ""
        }

        return errorMessage.value!!.isEmpty()
    }

    private fun isDuplicate() : Boolean {
        return false
    }

    private fun isSuitableLength() : Boolean {
        return inputText.value!!.length <= 8
    }
}