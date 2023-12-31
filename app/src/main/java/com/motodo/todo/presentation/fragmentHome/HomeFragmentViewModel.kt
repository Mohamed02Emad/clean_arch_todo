package com.motodo.todo.presentation.fragmentHome

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.motodo.todo.R
import com.motodo.todo.domain.models.Priority
import com.motodo.todo.domain.models.RemindBefroeTime
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.useCases.TodoUseCases
import com.motodo.todo.utils.DateHelper
import com.motodo.todo.utils.getHour
import com.motodo.todo.utils.getMinute
import com.motodo.todo.utils.isToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

const val TAG = "mohamed"

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    val useCases: TodoUseCases
) : ViewModel() {


    private val _todos = MutableLiveData<ArrayList<ToDo>?>(ArrayList())
    val todos: LiveData<ArrayList<ToDo>?> = _todos

    private val _isBottomSheetOpened = MutableLiveData<Boolean>(false)
    val isBottomSheetOpened: LiveData<Boolean> = _isBottomSheetOpened

    private val _currentDate = MutableLiveData<Date?>(null)
    val currentDate: LiveData<Date?> = _currentDate

    private val _dateChanged = MutableLiveData<Boolean>(true)
    val dateChanged: LiveData<Boolean> = _dateChanged

    private val _hasAlarm = MutableLiveData<Boolean>(true)
    val hasAlarm: LiveData<Boolean> = _hasAlarm

    private val _alarmTime = MutableLiveData<String?>(null)
    val alarmTime: LiveData<String?> = _alarmTime

    private val _title = MutableLiveData<String?>(null)
    val title: LiveData<String?> = _title

    private val _hasNotifyEnabled = MutableLiveData<Boolean>(true)
    val hasNotifyEnabled: LiveData<Boolean> = _hasNotifyEnabled

    private val _notifyBefore = MutableLiveData<RemindBefroeTime>(RemindBefroeTime.ONE_DAY)
    val notifyBefore: LiveData<RemindBefroeTime> = _notifyBefore

    private val _priority = MutableLiveData<Priority>(Priority.NONE)
    val priority: LiveData<Priority> = _priority

    private val _removedTodo = MutableLiveData<ToDo?>(null)
    val removedTodo: LiveData<ToDo?> = _removedTodo

    fun resetInsertTodoData() {
        _hasAlarm.value = true
        _alarmTime.value = null
        _title.value = null
        _hasNotifyEnabled.value = true
        _notifyBefore.value = RemindBefroeTime.ONE_DAY
        _priority.value = Priority.NONE
    }

    fun setHasAlarm(hasAlarm: Boolean) {
        _hasAlarm.value = hasAlarm
    }

    fun setAlarmTime(alarmTime: String?) {
        _alarmTime.value = alarmTime
    }

    fun setTitle(title: String?) {
        _title.value = title
    }

    fun setHasNotifyEnabled(hasNotifyEnabled: Boolean) {
        _hasNotifyEnabled.value = hasNotifyEnabled
    }

    fun setNotifyBefore(notifyBefore: CharSequence) {
        _notifyBefore.value =
            when (notifyBefore) {
                "1 day" -> {
                    RemindBefroeTime.ONE_DAY
                }
                "1 hour" -> {
                   RemindBefroeTime.ONE_HOUR
                }
                else -> {
                 RemindBefroeTime.FIFTEEN_MINUTE
                }
            }

    }

    fun setPriority(priority: CharSequence) {
        _priority.value =
            when (priority) {
                "High" -> {
                    Priority.HIGH
                }
                "Medium" -> {
                    Priority.MEDIUM
                }
                "Low" -> {
                    Priority.LOW
                }
                else -> {
                    Priority.NONE
                }
            }
    }

    fun dayChanged(date: Date) {
        viewModelScope.launch(Dispatchers.Main) {
            setCurrentDate(date)
        }
    }

    fun triggerBottomSheetState() {
        _isBottomSheetOpened.value = !_isBottomSheetOpened.value!!
    }

    fun isNewDate(date: Date): Boolean = currentDate.value == null || date != currentDate.value
    private suspend fun setCurrentDate(date: Date) {
        setDateChanged(true)
        _currentDate.value = date
        updateTodosList(date)
    }

    private suspend fun updateTodosList(date: Date) {
        _todos.value = useCases.getTodosUseCase(date)
    }

    suspend fun saveTodo(): ToDo? {
        if (title.value.isNullOrBlank()) {
            return null
        }
        val date = currentDate.value!!
        val day = DateHelper.getDay(date)
        val month = DateHelper.getMonthIndex(date)
        val year = DateHelper.getYearName(date)
        val todo = ToDo(
            title = title.value!!,
            hasAlarm = hasAlarm.value!!,
            alarmTime = if (hasAlarm.value!!) alarmTime.value!! else null,
            year = year,
            month = month,
            day = day,
            remindBefore = if (hasNotifyEnabled.value!!) notifyBefore.value!! else RemindBefroeTime.DO_NOT,
            priority = priority.value!!
        )
        saveTodoToDatabase(todo)
        if (todo.hasAlarm && isToday(day.toInt(), month.toInt(), year.toInt() , getHour(todo), getMinute(todo)) ) {
            setAlarmForTodo(todo)
        }
        return todo
    }

    private fun  setAlarmForTodo(todo: ToDo) {
        viewModelScope.launch {
            useCases.setAlarmForTodoUseCase(todo)
        }
    }

    private suspend fun saveTodoToDatabase(todo: ToDo) = withContext(Dispatchers.Main) {
        useCases.insertTodoUseCase(todo)
        _todos.value = useCases.sortTodosUseCase(_todos.value!!, todo)
    }

    suspend fun deleteTodos(item: ToDo) {
        _removedTodo.value = item
        _todos.value!!.remove(item)
        useCases.deleteTodoUseCase(item)
    }

    fun undoDeletion() = viewModelScope.launch {
        removedTodo.value?.let {todo ->
            saveTodoToDatabase(todo)
            if (todo.hasAlarm && isToday(todo.day.toInt(),todo.month.toInt(),todo.year.toInt() , getHour(todo), getMinute(todo)) ) {
                setAlarmForTodo(todo)
            }
            _removedTodo.value = null
        }
    }

    suspend fun updateTodo(todo: ToDo, index: Int) {
        useCases.updateTodoUseCase(todo)
        _todos.value?.set(index, todo)
    }

    fun setDateChanged(b: Boolean) {
        _dateChanged.value = b
    }

    val myCalendarViewManager = object : CalendarViewManager {
        override fun setCalendarViewResourceId(
            position: Int,
            date: Date,
            isSelected: Boolean
        ): Int {
            return if (isSelected) {
                R.layout.calendar_selected
            } else {
                R.layout.calendar_not_selected
            }
        }

        override fun bindDataToCalendarView(
            holder: SingleRowCalendarAdapter.CalendarViewHolder,
            date: Date,
            position: Int,
            isSelected: Boolean
        ) {
            holder.itemView.findViewById<TextView>(R.id.tv_day_of_month).text =
                DateHelper.getDay(date)
            holder.itemView.findViewById<TextView>(R.id.tv_day_name).text =
                DateHelper.getDayLetter(date)
        }
    }

    val mySelectionManager = object : CalendarSelectionManager {
        override fun canBeItemSelected(position: Int, date: Date): Boolean {
            return true
        }
    }


}