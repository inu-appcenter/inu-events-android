package org.inu.events

import android.app.Application
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.inu.events.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    companion object {
        @BindingAdapter("imageFromUrl")
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
            loadWithUrl(view, imageUrl)
        }

        @BindingAdapter("imageFromUuid")
        @JvmStatic
        fun bindImageFromUuid(view: ImageView, imageUuid: String?) {
            loadWithUuid(view, imageUuid)
        }

        @BindingAdapter("bindImageRadius4")
        @JvmStatic
        fun bindImage(view: ImageView, imageUuid: String?) {
            val displayMetrics = view.context.resources.displayMetrics
            val radiusWithPixel = (displayMetrics.density * 4).toInt()

            loadWithUuid(view, imageUuid, radiusWithPixel)
        }

        @BindingAdapter("app:photo_uuid")
        @JvmStatic
        fun setPhoto(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl)
                .transform(CenterCrop())
                .error(R.drawable.ic_default_photo)
                .placeholder(R.drawable.ic_default_photo)
                .into(view)
        }

        @BindingAdapter("layoutMarginBottom")
        @JvmStatic
        fun setLayoutMarginBottom(view: View, dimen: Float) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).let {
                it.bottomMargin = dimen.toInt()
                view.layoutParams = it
            }
        }

        private fun loadWithUuid(view: ImageView, imageUuid: String?, radiusWithPixel: Int = 0) {
            if(imageUuid?.trim()?.isBlank() != false) {
                Glide.with(view.context)
                    .load(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
                return
            }

            val imageUrl = "http://uniletter.inuappcenter.kr/images/$imageUuid"

            loadWithUrl(view, imageUrl, radiusWithPixel)
        }

        private fun loadWithUrl(view: ImageView, imageUrl: String?, radiusWithPixel: Int = 0) {
            if(imageUrl?.trim()?.isBlank() != false) {
                Glide.with(view.context)
                    .load(R.drawable.img_default)
                    .into(view)
                return
            }

            if(radiusWithPixel != 0) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .transform(CenterCrop(), RoundedCorners(radiusWithPixel))
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
            }
            else {
                Glide.with(view.context)
                    .load(imageUrl)
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(myModules)
        }

    }
}