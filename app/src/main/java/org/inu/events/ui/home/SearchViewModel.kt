package org.inu.events.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val eventRepository: EventRepository by inject()

    val searchText = MutableStateFlow("")
    private val category = MutableStateFlow(0)
    private val eventStatus = MutableStateFlow(false)

    var searchResult: MutableStateFlow<PagingData<Event>> = MutableStateFlow(PagingData.empty())
    private var job: Job? = null

    init {

        viewModelScope.launch {
            initSearch()
        }

    }

    @OptIn(FlowPreview::class)
    private suspend fun initSearch() {
        searchText.debounce(500).collect {
            job?.cancel()
            if (it.isEmpty()) {
                searchResult.value = PagingData.empty()
                return@collect
            }
            search().run {
                job = this
            }
        }
    }

    private fun search(): Job {
        return viewModelScope.launch {
            eventRepository.searchEvents(
                categoryId = category.value,
                eventStatus = eventStatus.value,
                content = searchText.value
            ).cachedIn(viewModelScope).collectLatest { pagingData ->
                searchResult.value = pagingData
            }
        }
    }
}