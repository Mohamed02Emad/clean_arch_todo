package com.motodo.todo.data.receivers.dailyCheck

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.motodo.todo.data.receivers.todosAlarm.TodosAlarmReceiver
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.presentation.fragmentHome.TAG
import com.motodo.todo.utils.createExactAlarm
import com.motodo.todo.utils.getCalendarForTomorrow
import java.util.Calendar

class DailyCheckReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm changed", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Daily check started ")
        val todo = ToDo()
        todo.title = "task 1"
        todo.alarmTime = "0:2 AM"
        TodosAlarmReceiver.setTodoAlarm(context, todo)
        setDailyCheck(context)
    }


    companion object {
        fun setDailyCheck(context: Context) {

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, DailyCheckReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = getCalendarForTomorrow()
            createExactAlarm(calendar, alarmManager, pendingIntent)

            Log.d(TAG, "Daily check requested ")
        }

    }


}


