package com.motodo.todo.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import com.motodo.todo.domain.models.RemindBefroeTime
import com.motodo.todo.domain.models.ToDo
import java.io.File
import java.util.Calendar

fun createExactAlarm(calendar: Calendar , alarmManager: AlarmManager , pendingIntent : PendingIntent){
    if (Build.VERSION.SDK_INT >= 31) {
        val canScheduleExactAlarms: Boolean = alarmManager.canScheduleExactAlarms()
        if (!canScheduleExactAlarms) {
//            throw SecurityException("App does not have the SCHEDULE_EXACT_ALARM permission")
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    } else {
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
fun getCalendarForTomorrow():Calendar {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    return calendar
}
fun getCalendarForTodoAlarm(todo: ToDo) =  Calendar.getInstance().apply {
    timeInMillis = System.currentTimeMillis()
    set(Calendar.HOUR_OF_DAY, getHour(todo))
    set(Calendar.MINUTE, getMinute(todo))
    set(Calendar.SECOND, 0)
}

fun getMinute(todo: ToDo): Int {
    val list = todo.alarmTime!!.split(":")
    val minute = list.last().split(" ").first().toInt()
    return minute
}

fun getHour(todo: ToDo): Int {
    val list = todo.alarmTime!!.split(":")
    var hour = list.first().toInt()
    if (todo.alarmTime!!.get(todo.alarmTime!!.length - 2) == 'P') {
        hour += 12
    }
    return hour
}

fun getUriOfCachedAudio(context: Context): Uri? {
    return try {
        val audioLocation = context.filesDir.absolutePath + File.separator + "alarm_audio.mp3"
        val cacheFile = File(audioLocation)
        if (!cacheFile.exists()) {
            null
        } else {
            Uri.parse(cacheFile.absolutePath)
        }
    } catch (e: Exception) {
        null
    }
}

fun setReminderCalendar(remindBefore: RemindBefroeTime, calendar: Calendar): Calendar? {
    when (remindBefore) {
        RemindBefroeTime.ONE_DAY -> calendar.add(Calendar.DAY_OF_YEAR, -1)
        RemindBefroeTime.ONE_HOUR -> calendar.add(Calendar.HOUR_OF_DAY, -1)
        RemindBefroeTime.FIFTEEN_MINUTE -> calendar.add(Calendar.MINUTE, -15)
        RemindBefroeTime.DO_NOT -> null
    }
    return calendar
}