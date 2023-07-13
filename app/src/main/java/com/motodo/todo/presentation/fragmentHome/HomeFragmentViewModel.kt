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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

const val TAG = "mohamed"
@HiltViewModel
class HomeFragmentViewModel @Inject constructor(val useCases : TodoUseCases) : ViewModel() {


    private val _todos = MutableLiveData<ArrayList<ToDo>?>(ArrayList())
    val todos: LiveData<ArrayList<ToDo>?> = _todos

    private val _isBottomSheetOpened = MutableLiveData<Boolean>(false)
    val isBottomSheetOpened: LiveData<Boolean> = _isBottomSheetOpened


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


    private val _currentDate = MutableLiveData<Date?>(null)
    val currentDate: LiveData<Date?> = _currentDate
    fun dayChanged(date: Date) {
        setCurrentDate(date)
    }

    fun triggerBottomSheetState() {
        _isBottomSheetOpened.value = !_isBottomSheetOpened.value!!
    }

    fun isNewDate(date: Date): Boolean = currentDate.value == null || date != currentDate.value
    private fun setCurrentDate(date: Date) {
        _currentDate.value = date
        updateTodosList(date)
    }

    private fun updateTodosList(date: Date) {
        CoroutineScope(Dispatchers.Main).launch{
            _todos.value = useCases.getTodosUseCase(date)
        }
    }

    fun saveTodo(): Boolean {
        if (title.value.isNullOrBlank()) {
            return false
        }
        val date = currentDate.value!!
        val todo = ToDo(
            title = title.value!!,
            hasAlarm = hasAlarm.value!!,
            alarmTime = if (hasAlarm.value!!) alarmTime.value!! else null,
            year = DateHelper.getYearName(date),
            month = DateHelper.getMonthName(date),
            day = DateHelper.getDay(date),
            remindBefore = if (hasNotifyEnabled.value!!) notifyBefore.value!! else RemindBefroeTime.DO_NOT,
            priority = priority.value!!
        )
        saveTodoToDatabase(todo)
        return true
    }

    private fun saveTodoToDatabase(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.insertUpdateTodoUseCase(todo)
            updateTodosList(currentDate.value!!)
        }
    }

    suspend fun deleteTodos(item: ToDo) {
        useCases.deleteTodoUseCase(item)
        updateTodosList(currentDate.value!!)
        withContext(Dispatchers.Main){
            _removedTodo.value = item
        }
    }

    suspend fun undoDeletion() {
        if (removedTodo.value != null) {
            useCases.insertUpdateTodoUseCase(removedTodo.value!!)
            updateTodosList(currentDate.value!!)
            withContext(Dispatchers.Main) {
                _removedTodo.value = null
            }
        }
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