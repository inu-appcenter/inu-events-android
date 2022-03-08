package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.entity.User
import org.inu.events.data.repository.BlockRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BlockedAccountViewModel : ViewModel(), KoinComponent {
    private val blockRepository: BlockRepository by inject()

    val blockedAccountList = MutableLiveData<List<User>>()

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val blockedUsers = blockRepository.getBlockUsers().map {
                it.user
            }
            blockedAccountList.postValue(blockedUsers)
        }
    }

    fun onClickCancelBlocking(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            blockRepository.deleteBlockUsers(
                AddBlockParams(userId)
            )
        }
    }
}