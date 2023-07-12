package com.motodo.todo.presentation.fragmentHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.motodo.todo.R
import com.motodo.todo.databinding.FragmentHomeBinding
import com.motodo.todo.presentation.recyclerViews.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
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
            openBottomSheet()
        }
    }
    private fun openBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_sheet_add_todo)
        dialog.setOnDismissListener {
            viewModel.triggerBottomSheetState()
            viewModel.resetInsertTodoData()
        }
        dialog.setOnShowListener {
            viewModel.triggerBottomSheetState()
        }
        setBottomSheetOnClicks(dialog)
        setBottomSheetObserver(dialog)
        setBottomSheetViews(dialog)
        dialog.show()
    }

    private fun setBottomSheetViews(dialog: BottomSheetDialog) {
        val timePicker = dialog.findViewById<TimePicker>(R.id.timePicker)
        val hour = timePicker?.hour
        val minute = timePicker?.minute
        setAlarmTimeText(hour!!, minute!!)
    }

    private fun setBottomSheetObserver(dialog: BottomSheetDialog) {
        viewModel.alarmTime.observe(viewLifecycleOwner) { time ->
            val text = if (time.isNullOrBlank()) "" else time.toString()
            dialog.findViewById<TextView>(R.id.tv_time)?.text = text
        }

        viewModel.hasAlarm.observe(viewLifecycleOwner){hasAlarm ->
            dialog.findViewById<TextView>(R.id.tv_time)?.text =  if (hasAlarm){
                viewModel.alarmTime.value
            }else{
                "Alarm"
            }
        }
    }

    private fun setBottomSheetOnClicks(dialog: BottomSheetDialog) {
        val listOfReminderChips = listOf<Button>(
            dialog.findViewById(R.id.chip_one_day)!!,
            dialog.findViewById(R.id.chip_one_hour)!!,
            dialog.findViewById(R.id.chip_fifteen_minutes)!!
        )

        val listOfPriorityChips = listOf<Button>(
            dialog.findViewById(R.id.chip_priority_high)!!,
            dialog.findViewById(R.id.chip_priority_medium)!!,
            dialog.findViewById(R.id.chip_priority_low)!!,
            dialog.findViewById(R.id.chip_priority_none)!!
        )

        dialog.apply {

            findViewById<SwitchCompat>(R.id.sw_alarm)?.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setHasAlarm(isChecked)
                findViewById<TimePicker>(R.id.timePicker)?.isGone = !isChecked
            }

            findViewById<SwitchCompat>(R.id.sw_remind_me)?.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setHasNotifyEnabled(isChecked)
                findViewById<LinearLayout>(R.id.reminderChipGroup)?.isGone = !isChecked
            }

            findViewById<EditText>(R.id.et_title)?.doOnTextChanged { text, _, _, _ ->
                try {
                    viewModel.setTitle(text.toString())
                } catch (_: Exception) {
                    viewModel.setTitle(null)
                }
            }

            for (chip in listOfReminderChips) {
                chip.setOnClickListener {
                    unCheckAllChips(listOfReminderChips)
                    selectChip(chip)
                    viewModel.setNotifyBefore(chip.text)
                }
            }

            for (chip in listOfPriorityChips) {
                chip.setOnClickListener {
                    unCheckAllChips(listOfPriorityChips)
                    selectChip(chip)
                    viewModel.setPriority(chip.text)
                }
            }

            findViewById<TimePicker>(R.id.timePicker)?.setOnTimeChangedListener { view, hourOfDay, minute ->
                setAlarmTimeText(hourOfDay, minute)
            }

            findViewById<Button>(R.id.btn_save)?.setOnClickListener {
               val isSaved  = viewModel.saveTodo()
                if (isSaved) {
                    dialog.dismiss()
                }else{
                    Toast.makeText(requireContext(),"Complete all Fields",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAlarmTimeText(hourOfDay: Int, minute: Int) {
        var string = ""
        if (hourOfDay > 12) string += "${hourOfDay - 12}:"
        else string += "${hourOfDay}:"
        string += "${minute} "
        if (hourOfDay > 12) string += "PM"
        else string += "AM"
        viewModel.setAlarmTime(string)
    }

    private fun unCheckAllChips(listOfChips: List<Button>) {
        for (chip in listOfChips) {
            chip.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.chip_unselected)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun selectChip(chip: Button) {
        chip.background = ContextCompat.getDrawable(requireContext(), R.drawable.chip_selected)
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary_blue))
    }

    private fun setupRecyclerView() {
        myAdapter = TodosAdapter()
        myAdapter.differ.submitList(viewModel.todos.value)
        binding.rvTodos.adapter = myAdapter
    }

    private fun setObservers() {
        viewModel.apply {

            todos.observe(viewLifecycleOwner) { newList ->
                myAdapter.differ.submitList(newList)
            }

            isBottomSheetOpened.observe(viewLifecycleOwner) { state ->
                binding.semiBlackWall.isGone = !state
            }
        }

    }

    private val myCalendarChangesObserver = object : CalendarChangesObserver {
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