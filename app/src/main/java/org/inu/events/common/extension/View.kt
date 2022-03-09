package org.inu.events.common.extension

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun View.setBackgroundTintResource(@ColorRes resId: Int) {
    val drawable = DrawableCompat.wrap(this.background)
    val color = ContextCompat.getColor(this.context, resId)

    DrawableCompat.setTint(drawable, color)
}