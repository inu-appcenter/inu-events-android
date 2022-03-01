package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.UpdateUserParams
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.EventRepository
import org.inu.events.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateProfileViewModel : ViewModel(), KoinComponent {
    private val userService: UserService by inject()
    private val eventRepository: EventRepository by inject()

    val user = MutableLiveData<User>()
    val updatePhotoEvent = SingleLiveEvent<Any>()

    val inputText = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val finishEvent = SingleLiveEvent<Any>()
    var imageUuid: String? = null

    init {
        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.Main).launch {
            user.value = userService.getUserInfo()
            inputText.value = user.value!!.nickname
        }
    }

    fun validateNickname(): Boolean {
        errorMessage.value = when {
            inputText.value!!.isEmpty() -> "공백은 닉네임으로 설정하실 수 없습니다."
            !isSuitableLength() -> "닉네임은 8자 이내로 설정해 주세요."
            else -> ""
        }

        return errorMessage.value!!.isEmpty()
    }

    private fun isSuitableLength(): Boolean {
        return inputText.value!!.length <= 8
    }

    fun onClickFinish() {
        val newUser = UpdateUserParams(
            nickname = inputText.value!!,
            imageUuid = imageUuid
        )

        CoroutineScope(Dispatchers.Main).launch {
            userService.updateUser(newUser)
            finishEvent.call()
        }
    }

    fun onClickUpdatePhoto() {
        updatePhotoEvent.call()
    }

    fun uploadImage(image: MultipartBody.Part) {
        CoroutineScope(Dispatchers.IO).launch {
            imageUuid = eventRepository.uploadImage(image).uuid
        }
    }

    fun resetToDefaultImage() {
        val newUser = UpdateUserParams(
            nickname = inputText.value!!,
            imageUuid = null
        )

        CoroutineScope(Dispatchers.IO).launch {
            userService.resetDefaultImage(newUser)
            loadData()
        }
    }
}