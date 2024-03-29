package org.inu.events.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.inu.events.data.httpservice.EventHttpService
import org.inu.events.data.model.entity.Event

private const val STARTING_KEY = 0

class EventPagingSource(
    private val httpService: EventHttpService,
    private val categoryId: Int = 0,
    private val eventStatus: Boolean = false,
) : PagingSource<Int, Event>() {

    override fun getRefreshKey(state: PagingState<Int, Event>): Int {
        return ensureValidKey(key = 0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        val start = params.key ?: STARTING_KEY
        val page = start / params.loadSize

        return withContext(Dispatchers.IO) {
            val data = httpService.fetchEvents(
                pageNum = page,
                pageSize = params.loadSize,
                categoryId = categoryId,
                eventStatus = eventStatus
            ).execute().body()!!
            LoadResult.Page(
                data = data,
                prevKey = when (start) {
                    STARTING_KEY -> null
                    else -> ensureValidKey(key = start - params.loadSize)
                },
                nextKey = if (data.size == params.loadSize) start + params.loadSize else null
            )
        }
    }

    private fun ensureValidKey(key: Int) = Integer.max(STARTING_KEY, key)
}