package org.inu.events

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class UniLetterToolbar(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {
    private lateinit var title: String

    init {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.UniLetterToolbar,
            0, 0
        ).apply {
            try {
                title = getString(R.styleable.UniLetterToolbar_title) ?: ""
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        inflate(context, R.layout.view_toolbar, this)

        val titleTextView = findViewById<TextView>(R.id.title)

        titleTextView.text = title
    }

    fun setOnBackListener(action: (view: View)->Unit) {
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            action(it)
        }
    }

    fun setTitle(text: String) {
        findViewById<TextView>(R.id.title).text = text
    }
}