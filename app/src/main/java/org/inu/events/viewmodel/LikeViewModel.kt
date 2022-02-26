package org.inu.events.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.inu.events.BackButtonListener
import org.inu.events.DetailActivity
import org.inu.events.dialog.TwoButtonDialog
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.LikeRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LikeViewModel : ViewModel(), KoinComponent {
    private val likeRepository: LikeRepository by inject()

    val eventList = MutableLiveData<List<Event>>()
    val onClickBackEvent = SingleLiveEvent<Any>()

    val backButtonListener = object : BackButtonListener {
        override fun invoke(view: View) {
            onClickBackEvent.call()
        }
    }

    fun loadLikes() {
        CoroutineScope(Dispatchers.Main).launch {
            val eventsDeferred = async(Dispatchers.IO) { likeRepository.getLikes() }
            val eventList = eventsDeferred.await()
            this@LikeViewModel.eventList.postValue(eventList)
        }
    }

    fun onClickLikeIcon(eventId: Int, view: View) {
        TwoButtonDialog("저장을 취소하시겠어요?") { deleteLike(eventId) }.show(view.context)
    }

    private fun deleteLike(eventId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val likeParam = LikeParam(eventId = eventId)
            likeRepository.deleteLike(likeParam)
            loadLikes()
        }
    }

    fun onClickDetail(view: View, event: Event) {
        with(view.context) {
            startActivity(DetailActivity.callingIntent(this, event.id, event.wroteByMe))
        }
    }
}