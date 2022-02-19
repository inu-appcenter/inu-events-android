package org.inu.events.common.util

import android.content.Context
import android.os.PowerManager
import android.view.WindowManager

class WakeLockUtil {
    private var wLock : PowerManager.WakeLock? = null
    fun acquireCpuWakeLock(context: Context){
        if (wLock != null) return
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wLock = pm.newWakeLock(10 or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, "myapp:TAG")
        //
        wLock!!.acquire(1000)
    }

    fun releaseCpuWakeLock(){
        if (wLock != null) {
            wLock!!.release()
            wLock = null
        }

    }
}