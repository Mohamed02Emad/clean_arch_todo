package com.motodo.todo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.motodo.todo.R
import com.motodo.todo.data.receivers.muteAlarmReceiver.MuteAlarmReceiver
import com.motodo.todo.presentation.MainActivity
import java.util.Calendar


fun isOldDate(day: Int, month: Int, year: Int): Boolean {
    val inputDate = Calendar.getInstance()
    inputDate.set(year, month, day)
    val todayDate = Calendar.getInstance()
    todayDate.apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    return inputDate.before(todayDate)
}

fun isToday(day: Int, month: Int, year: Int): Boolean {
    val todayDate = Calendar.getInstance()
    return year == todayDate.get(Calendar.YEAR) &&
            month == todayDate.get(Calendar.MONTH) &&
            day == todayDate.get(Calendar.DAY_OF_MONTH)
}


fun showNotification(
    context: Context,
    title: String?,
    reqCode: Int,
    hasAlarm: Boolean
) {

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(
            context,
            reqCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val muteIntent = Intent(context, MuteAlarmReceiver::class.java).apply {
        action = "ACTION_STOP_ALARM"
    }
    val stopAlarmPendingIntent: PendingIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, reqCode, muteIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(
                context,
                reqCode,
                muteIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        }

    val CHANNEL_ID = "main_chanel"

    val body = if(hasAlarm){
        "your alarm is here"
    }else{
        "we remind you of your task"
    }

    val notificationBuilder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)
    notificationBuilder.apply {
        setSmallIcon(R.drawable.icon)
        setContentTitle(title)
        setContentText(body)
        setAutoCancel(true)
        setContentIntent(pendingIntent)
        setSound(null)
        if (hasAlarm) {
            addAction(R.drawable.ic_alarm, "Stop Alarm", stopAlarmPendingIntent)
        }
    }


    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val name: CharSequence = "Main Notifications Channel"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val mChannel = NotificationChannel(CHANNEL_ID, name, importance)

    notificationManager.createNotificationChannel(mChannel)

    notificationManager.notify(
        reqCode,
        notificationBuilder.build()
    )

}

