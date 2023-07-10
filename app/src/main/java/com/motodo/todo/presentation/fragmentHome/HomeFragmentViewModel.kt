package com.motodo.todo.presentation.fragmentHome

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.motodo.todo.R
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.useCases.GetDateToDosUseCase
import com.motodo.todo.utils.DateHelper
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.log

const val TAG = "mohamed"
class HomeFragmentViewModel : ViewModel() {

    private val getTodosForDateUseCase  = GetDateToDosUseCase()

    private val _todos  = MutableLiveData<List<ToDo>?>()
    val todos  : LiveData<List<ToDo>?> = _todos

    private val _currentDate  = MutableLiveData<Date?>(null)
    val currentDate  : LiveData<Date?> = _currentDate
    fun dayChanged(date: Date) {
        setCurrentDate(date)
       // Log.d(TAG, "dayChanged: " + DateUtils.getDay1LetterName(date))
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