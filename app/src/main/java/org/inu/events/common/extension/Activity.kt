package org.inu.events.common.extension

import android.app.Activity

fun Activity.getIntExtra(name: String): Int? {
    val extras = intent.extras ?: return null

    return if (intent.hasExtra(name)) {
        extras.getInt(name, -9999).takeIf { it != -9999 }
    } else {
        null
    }
}