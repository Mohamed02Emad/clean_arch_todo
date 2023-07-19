package com.motodo.todo.data.receivers.dailyCheck

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.motodo.todo.data.receivers.todosAlarm.TodosAlarmReceiver
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.useCases.TodoUseCases
import com.motodo.todo.presentation.fragmentHome.TAG
import com.motodo.todo.utils.createExactAlarm
import com.motodo.todo.utils.getCalendarForTomorrow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DailyCheckReceiver : BroadcastReceiver() {

    @Inject
    lateinit var useCases: TodoUseCases

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentDate = Date()
            val todos = useCases.getTodosUseCase(currentDate)
            for (todo in todos) {
                    TodosAlarmReceiver.setTodoAlarm(context, todo)
            }
        }
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
        }

    }


}


