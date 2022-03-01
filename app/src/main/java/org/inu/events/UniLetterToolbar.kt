package org.inu.events

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class UniLetterToolbar(context: Context, attrs: AttributeSet) : Toolbar(context, attrs) {
    private var title: String = ""
    set(value) {
        field = value
        rootView?.findViewById<TextView>(R.id.title)?.text = value
    }

    private var showInformationIcon: Boolean = false

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
                showInformationIcon = getBoolean(R.styleable.UniLetterToolbar_showInformationIcon, false)
                title = getString(R.styleable.UniLetterToolbar_title) ?: ""
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        inflate(context, R.layout.view_toolbar, this)

        val titleTextView = findViewById<TextView>(R.id.title)
        val informationIcon = findViewById<ImageButton>(R.id.information_icon)

        titleTextView.text = title
        informationIcon.visibility = if(showInformationIcon) View.VISIBLE else View.GONE
    }

    fun setOnBackListener(action: (view: View)->Unit) {
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            action(it)
        }
    }

    /**
     * title의 setter를 불러서 타이틀을 설정하려면,
     * 이 시그니처를 오버라이드해야 합니다.
     */
    override fun setTitle(title: CharSequence?) {
        this.title = title.toString()
    }
}