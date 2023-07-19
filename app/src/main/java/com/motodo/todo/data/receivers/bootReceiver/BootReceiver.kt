package com.motodo.todo.data.receivers.bootReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.motodo.todo.data.receivers.dailyCheck.DailyCheckReceiver

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED){
            DailyCheckReceiver.setDailyCheck(context!!)
        }
    }
}