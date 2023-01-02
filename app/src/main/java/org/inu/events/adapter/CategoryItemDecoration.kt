package org.inu.events.adapter

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class CategoryItemDecoration(
    private val spanCount: Int = 2,
    private val spacing: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        val metrics: DisplayMetrics = view.context.resources.displayMetrics
        val spacingPixel = (metrics.density * spacing).toInt()

        if(column == 0) {
            outRect.right = spacingPixel
        }
        else {
            outRect.left = spacingPixel
        }
    }
}