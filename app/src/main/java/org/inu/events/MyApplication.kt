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
            if(imageUrl?.trim()?.isBlank() != false) {
                Glide.with(view.context)
                    .load(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
                return
            }

            Glide.with(view.context)
                .load(imageUrl)
                .error(R.drawable.img_default)
                .placeholder(R.drawable.img_default)
                .into(view)
        }

        @BindingAdapter("imageFromUuid")
        @JvmStatic
        fun bindImageFromUuid(view: ImageView, imageUuid: String?) {

            if(imageUuid?.trim()?.isBlank() != false) {
                Glide.with(view.context)
                    .load(R.drawable.img_default)
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
                return
            }

            loadWithGlide(view, imageUuid)
        }


        @BindingAdapter("bindImageRadius4")
        @JvmStatic
        fun bindImage(view: ImageView, imageUuid: String?) {
            val displayMetrics = view.context.resources.displayMetrics
            val radiusWithPixel = (displayMetrics.density * 4).toInt()

            loadWithGlide(view, imageUuid, radiusWithPixel)
        }

        @BindingAdapter("app:photo_uuid")
        @JvmStatic
        fun setPhoto(view: ImageView, imageUrl: String?) {
            loadWithGlide(view, imageUrl)
        }

        @BindingAdapter("layoutMarginBottom")
        @JvmStatic
        fun setLayoutMarginBottom(view: View, dimen: Float) {
            (view.layoutParams as ViewGroup.MarginLayoutParams).let {
                it.bottomMargin = dimen.toInt()
                view.layoutParams = it
            }
        }

        private fun loadWithGlide(view: ImageView, imageUuid: String?, radiusWithPixel: Int = 0) {
            val imageUrl = "http://uniletter.inuappcenter.kr/images/$imageUuid"

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