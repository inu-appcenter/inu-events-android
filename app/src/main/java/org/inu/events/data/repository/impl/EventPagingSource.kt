package org.inu.events.data.repository.impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.model.entity.Event

private val STARTING_KEY = 0

class EventPagingSource(
    private val httpService: EventHttpService
) : PagingSource<Int, Event>() {


    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val event = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = event.id - (state.config.pageSize / 2))
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val start = params.key ?: STARTING_KEY
        val page = start / 20

        return LoadResult.Page(
            data = httpService.fetchEvent(pageNum = page, pageSize = params.loadSize).execute().body()!!,
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = start - params.loadSize)
            },
            nextKey = start + params.loadSize
        )
    }

    private fun ensureValidKey(key: Int) = Integer.max(STARTING_KEY, key)
}