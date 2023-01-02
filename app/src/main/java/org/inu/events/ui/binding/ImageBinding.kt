package org.inu.events.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.inu.events.R
import org.inu.events.common.util.GlideUtil

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    GlideUtil.loadWithUrl(view, imageUrl)
}

@BindingAdapter("imageFromUuid")
fun bindImageFromUuid(view: ImageView, imageUuid: String?) {
    GlideUtil.loadWithUuid(view, imageUuid)
}

@BindingAdapter("bindImageRadius4")
fun bindImage(view: ImageView, imageUuid: String?) {
    val displayMetrics = view.context.resources.displayMetrics
    val radiusWithPixel = (displayMetrics.density * 4).toInt()

    GlideUtil.loadWithUuid(view, imageUuid, radiusWithPixel)
}

@BindingAdapter("app:photo_url")
fun setPhoto(view: ImageView, imageUrl: String?) {
    GlideUtil.loadWithUrl(view, imageUrl, default = R.drawable.ic_default_photo)
}