package com.motodo.todo.presentation.fragmentHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.motodo.todo.R
import com.motodo.todo.databinding.FragmentHomeBinding
import com.motodo.todo.presentation.recyclerViews.TodosAdapter
import com.motodo.todo.utils.DateHelper
import java.util.Date

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var myAdapter : TodosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRowCalendar()
        setObservers()

    }

    private fun setupRecyclerView() {
        myAdapter = TodosAdapter()
        myAdapter.differ.submitList(viewModel.todos.value)
        binding.rvTodos.adapter = myAdapter

    }

    private fun setObservers() {
     viewModel.todos.observe(viewLifecycleOwner){newList ->
         myAdapter.differ.submitList(newList)
         //Toast.makeText(requireContext(), "Observers", Toast.LENGTH_SHORT).show()
     }
    }

    val myCalendarChangesObserver = object : CalendarChangesObserver {
        override fun whenWeekMonthYearChanged(
            weekNumber: String,
            monthNumber: String,
            monthName: String,
            year: String,
            date: Date
        ) {
            super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
        }

        override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
            super.whenSelectionChanged(isSelected, position, date)
            if (viewModel.isNewDate(date)) {
                viewModel.dayChanged(date)
            }
        }

        override fun whenCalendarScrolled(dx: Int, dy: Int) {
            super.whenCalendarScrolled(dx, dy)
        }

        override fun whenSelectionRestored() {
            super.whenSelectionRestored()
        }

        override fun whenSelectionRefreshed() {
            super.whenSelectionRefreshed()
        }

    }

    private fun setupRowCalendar() {
        binding.mainSingleRowCalendar.apply {
            calendarViewManager = viewModel.myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = viewModel.mySelectionManager
            futureDaysCount = 30
            includeCurrentDate = true
            init()
            select(0)
        }
    }


}