package org.inu.events.common.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Activity.getIntExtra(name: String): Int? {
    val extras = intent.extras ?: return null

    return if (intent.hasExtra(name)) {
        extras.getInt(name, -9999).takeIf { it != -9999 }
    } else {
        null
    }
}

fun Activity.getBooleanExtra(name: String): Boolean? {
    val extras = intent.extras ?: return null

    return if (intent.hasExtra(name)) {
        extras.getBoolean(name, false).takeIf { it != null }
    } else {
        null
    }
}

fun ComponentActivity.registerForActivityResult(onResult: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onResult(it)
    }
}