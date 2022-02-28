package org.inu.events.binding

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.inu.events.R
import org.inu.events.objects.GlideUtil

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

@BindingAdapter("app:photo_uuid")
fun setPhoto(view: ImageView, imageUrl: String?) {
    GlideUtil.loadWithUrl(view, imageUrl, R.drawable.ic_default_photo)
}