package com.motodo.todo.data.receivers.todosAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.presentation.fragmentHome.TAG
import com.motodo.todo.utils.createExactAlarm
import com.motodo.todo.utils.getCalendarForTodoAlarm
import com.motodo.todo.utils.showNotification

class TodosAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val todo: ToDo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            intent?.getSerializableExtra("todo", ToDo::class.java) as ToDo
        } else {
            intent?.getSerializableExtra("todo") as ToDo
        }

        try {
            showNotification(context!!, todo.title, todo.id)
        } catch (_: Exception) {
        }
    }

    companion object {
        fun setTodoAlarm(context: Context, todo: ToDo) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TodosAlarmReceiver::class.java)
            try {
                intent.putExtra("todo", todo)
            } catch (_: Exception) {
                return
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, todo.id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = getCalendarForTodoAlarm(todo)
            createExactAlarm(calendar, alarmManager, pendingIntent)
        }
    }
}