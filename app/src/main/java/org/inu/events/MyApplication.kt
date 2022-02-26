package org.inu.events

import android.app.Application
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.inu.events.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*


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

        @BindingAdapter("app:photo_uuid")
        @JvmStatic
        fun setPhoto(view: ImageView, uuid: String?) {
            Glide.with(view.context)
                .load("http://uniletter.inuappcenter.kr/images/$uuid")
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