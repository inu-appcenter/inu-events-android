package org.inu.events.common.db

import android.content.Context
import android.preference.PreferenceManager
import android.text.TextUtils
import androidx.core.content.edit

/**
 * Credits to kcochibili.
 * See https://github.com/kcochibili/TinyDB--Android-Shared-Preferences-Turbo
 */
class SharedPreferenceWrapper(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putArrayInt(key: String, arrayInt: Array<Int>) {
        preferences.edit {
            putString(key, TextUtils.join("‚‗‚", arrayInt))
        }
    }

    fun getArrayInt(key: String): Array<Int>? {
        val record = preferences.getString(key, null) ?: return null

        return TextUtils.split(record, "‚‗‚").map { it.toInt() }.toTypedArray()
    }

    fun putString(key: String, value: String?) {
        preferences.edit {
            putString(key, value)
        }
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String, default: Int = -1): Int {
        return preferences.getInt(key, default)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit {
            putLong(key, value)
        }
    }

    fun getLong(key: String, default: Long = -1): Long {
        return preferences.getLong(key, default)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return preferences.getBoolean(key, default)
    }
}