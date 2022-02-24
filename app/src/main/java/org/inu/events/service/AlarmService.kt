package org.inu.events.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.inu.events.common.util.WakeLockUtil


class AlarmService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    private val wakeLockUtil = WakeLockUtil()

    override fun onCreate() {
        super.onCreate()

        wakeLockUtil.acquireCpuWakeLock(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        wakeLockUtil.releaseCpuWakeLock()
    }
}