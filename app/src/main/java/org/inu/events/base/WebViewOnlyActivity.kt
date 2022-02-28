package org.inu.events.base

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import org.inu.events.R
import org.inu.events.databinding.ActivityWebViewOnlyBinding

abstract class WebViewOnlyActivity : BaseActivity<ActivityWebViewOnlyBinding>() {
    abstract val toolbarTitle: String
    abstract val pageUrl: String

    override val layoutResourceId = R.layout.activity_web_view_only

    @SuppressLint("SetJavaScriptEnabled")
    override fun dataBinding() {
        with(binding.webView) {
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                    with(binding.progressBar) {
                        progress = newProgress
                        if (newProgress == 100) {
                            visibility = View.GONE
                        }
                    }
                }
            }

            with(settings) {
                javaScriptEnabled = true
                defaultTextEncodingName = "utf-8"
            }

            loadUrl(pageUrl)
        }

        with(binding.toolbar) {
            title = toolbarTitle

            setOnBackListener {
                finish()
            }
        }
    }
}