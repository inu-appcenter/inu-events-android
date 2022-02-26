package org.inu.events

import android.view.View

interface BackButtonListener {
    operator fun invoke(view: View)
}