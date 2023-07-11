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
import com.motodo.todo.domain.useCases.GetDateToDosUseCase
import com.motodo.todo.utils.DateHelper
import kotlinx.coroutines.launch
import java.util.Date

const val TAG = "mohamed"
class HomeFragmentViewModel : ViewModel() {

    private val getTodosForDateUseCase = GetDateToDosUseCase()

    private val _todos = MutableLiveData<List<ToDo>?>()
    val todos: LiveData<List<ToDo>?> = _todos

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
                "In 24 hours" -> {
                    RemindBefroeTime.ONE_DAY
                }
                "1 hour before" -> {
                   RemindBefroeTime.ONE_HOUR
                }
                else -> {
                 RemindBefroeTime.FIFTEEN_MINUTE
                }
            }

    }

    fun setPriority(priority: Priority) {
        _priority.value = priority
    }


    private val _currentDate = MutableLiveData<Date?>(null)
    val currentDate: LiveData<Date?> = _currentDate
    fun dayChanged(date: Date) {
        setCurrentDate(date)
        // Log.d(TAG, "dayChanged: " + DateUtils.getDay1LetterName(date))
    }

    fun triggerBottomSheetState() {
        _isBottomSheetOpened.value = !_isBottomSheetOpened.value!!
    }

    fun isNewDate(date: Date): Boolean  = currentDate.value == null || date != currentDate.value
    private fun setCurrentDate(date: Date) {
       _currentDate.value = date
        updateTodos(date)
    }

    private fun updateTodos(date: Date) {
        viewModelScope.launch {
            _todos.value = getTodosForDateUseCase(date).value
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