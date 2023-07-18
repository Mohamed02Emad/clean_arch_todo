package com.motodo.todo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.motodo.todo.R
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.presentation.MainActivity
import java.util.Calendar


fun isOldDate(day: Int, month: Int, year: Int): Boolean {
    val inputDate = Calendar.getInstance()
    inputDate.set(year, month, day)
    val todayDate = Calendar.getInstance()
    return inputDate.before(todayDate)
}

fun showNotification(
    context: Context,
    title: String?,
    reqCode: Int
) {

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val CHANNEL_ID = "main_chanel"

    val notificationBuilder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText("Your alarm is here")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(null)


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
