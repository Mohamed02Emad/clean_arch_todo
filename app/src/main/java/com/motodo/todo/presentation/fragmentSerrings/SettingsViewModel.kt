package com.motodo.todo.presentation.fragmentSerrings

import androidx.lifecycle.ViewModel
import com.motodo.todo.domain.useCases.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(val useCases: TodoUseCases) : ViewModel(){


    fun setNewAlarmSound(newAlarmSound: Any) {

    }

}