package com.motodo.todo.data.receivers.alarmReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.motodo.todo.presentation.fragmentHome.TAG
import java.util.Calendar

class DailyCheckReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm changed", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onReceive: sdgsg ")
    }

    companion object {
        fun setDailyCheck(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyCheckReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 1)
                set(Calendar.SECOND, 0)
                add(Calendar.DAY_OF_YEAR, 1)
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                50000,
                pendingIntent
            )

        }

    }
}


