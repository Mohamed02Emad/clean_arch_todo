package com.motodo.todo.service.receiverDateChanged

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.motodo.todo.domain.useCases.TodoUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class DateChangedReceiver @Inject constructor(val useCases: TodoUseCases): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
           CoroutineScope(Dispatchers.Main).launch {
               val calendar: Calendar = Calendar.getInstance()
               val today: Date = calendar.time
               val listOfTodayTodos = useCases.getTodosUseCase(today)
           }
        }
    }
}