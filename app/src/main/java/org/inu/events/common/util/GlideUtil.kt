package org.inu.events.common.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.inu.events.R

object GlideUtil {
    fun loadWithUuid(view: ImageView, imageUuid: String?, radiusWithPixel: Int = 0, default: Int = R.drawable.img_default) {
        if(imageUuid?.trim()?.isBlank() != false) {
            Glide.with(view.context)
                .load(default)
                .error(default)
                .placeholder(default)
                .into(view)
            return
        }

        val imageUrl = "http://uniletter.inuappcenter.kr/images/$imageUuid"

        loadWithUrl(view, imageUrl, radiusWithPixel)
    }

    fun loadWithUrl(view: ImageView, imageUrl: String?, radiusWithPixel: Int = 0, default: Int = R.drawable.img_default) {
        if(imageUrl?.trim()?.isBlank() != false) {
            Glide.with(view.context)
                .load(default)
                .into(view)
            return
        }

        if(radiusWithPixel != 0) {
            Glide.with(view.context)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(radiusWithPixel))
                .error(default)
                .placeholder(default)
                .into(view)
        }
        else {
            Glide.with(view.context)
                .load(imageUrl)
                .error(default)
                .placeholder(default)
                .into(view)
        }
    }
}