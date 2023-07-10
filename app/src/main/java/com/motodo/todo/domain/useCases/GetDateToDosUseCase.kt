package com.motodo.todo.domain.useCases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.motodo.todo.domain.models.Priority
import com.motodo.todo.domain.models.ToDo
import java.util.Date

class GetDateToDosUseCase {
    suspend operator fun invoke(date: Date): MutableLiveData<List<ToDo>?> {

        //todo : should get data from repository

        val todos  = MutableLiveData<List<ToDo>?>()
        todos.value = listOf(
            ToDo(
                0,
                "Task 1",
                true ,
                "1:30 AM",
                false,
                date,
                priority = Priority.HIGH
            ),
            ToDo(
                5,
                "Task 6",
                false ,
                "3:00 AM",
                false,
                date,
                priority = Priority.MEDIUM
            ),
            ToDo(
                3,
                "Task 4",
                false ,
                "3:30 AM",
                false,
                date,
                priority = Priority.LOW
            ),
            ToDo(
                1,
                "Task 2",
                false ,
                "2",
                true,
                date,
                priority = Priority.MEDIUM
            ),
            ToDo(
                2,
                "Task 3",
                true ,
                "3:30 AM",
                true,
                date,
                priority = Priority.LOW
            ),
            ToDo(
                4,
                "Task 5",
                false ,
                "4:30 AM",
                true,
                date,
                priority = Priority.HIGH
            ),
            ToDo(
                6,
                "Task 7",
                false ,
                "3:30 AM",
                true,
                date,
                priority = Priority.LOW
            )
        )
        return todos
    }
}