package org.inu.events.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.model.entity.Event

private const val STARTING_KEY = 0

class EventSearchPagingSource(
    private val httpService: EventHttpService,
    private val categoryId: Int,
    private val eventStatus: Boolean,
    private val content: String,
) : PagingSource<Int, Event>() {

    private suspend fun getData(pageNum: Int, pageSize: Int): List<Event> {
        return httpService.searchEvents(
            categoryId = categoryId,
            eventStatus = eventStatus,
            content = content,
            pageNum = pageNum,
            pageSize = pageSize,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val start = params.key ?: STARTING_KEY
        val page = start / params.loadSize
        val data = getData(
            pageNum = page,
            pageSize = params.loadSize
        )

        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = if (data.size == params.loadSize) start + params.loadSize else null
        )
    }
}