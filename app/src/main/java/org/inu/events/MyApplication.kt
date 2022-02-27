package org.inu.events

import android.app.Application
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.inu.events.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {
    companion object {
        @BindingAdapter("imageFromUrl")
        @JvmStatic
        fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .error(R.drawable.img_default)
                    .placeholder(R.drawable.img_default)
                    .into(view)
            } else {
                Glide.with(view.context)
                    .load(imageUrl)
                    .into(view)
            }
        }


        @BindingAdapter("bindImageRadius4")
        @JvmStatic
        fun bindImage(view: ImageView, imageUrl: String?) {
            val displayMetrics = view.context.resources.displayMetrics
            val radiusWithPixel = (displayMetrics.density * 4).toInt()

            Glide.with(view.context)
                .load(imageUrl)
                .transform(CenterCrop(), RoundedCorners(radiusWithPixel))
                .error(R.drawable.img_default)
                .placeholder(R.drawable.img_default)
                .into(view)
        }

        @BindingAdapter("app:photo_url")
        @JvmStatic
        fun setPhoto(view: ImageView, imageUrl: String?) {
            Glide.with(view.context)
                .load(imageUrl)
                .error(R.drawable.default_profile_background)
                .placeholder(R.drawable.default_profile_background)
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

    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(myModules)
        }

    }
}