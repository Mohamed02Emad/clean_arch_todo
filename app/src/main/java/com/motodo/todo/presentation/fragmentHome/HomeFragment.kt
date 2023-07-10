package com.motodo.todo.presentation.fragmentHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.motodo.todo.databinding.FragmentHomeBinding
import com.motodo.todo.presentation.recyclerViews.TodosAdapter
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
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnAddNewTodo.setOnClickListener {
            Toast.makeText(requireContext(), "Add new task", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        myAdapter = TodosAdapter()
        myAdapter.differ.submitList(viewModel.todos.value)
        binding.rvTodos.adapter = myAdapter
    }

    private fun setObservers() {
     viewModel.todos.observe(viewLifecycleOwner){newList ->
         myAdapter.differ.submitList(newList)
     }
    }

    val myCalendarChangesObserver = object : CalendarChangesObserver {
        override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
            super.whenSelectionChanged(isSelected, position, date)
            if (viewModel.isNewDate(date)) {
                viewModel.dayChanged(date)
            }
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