package com.motodo.todo.domain.useCases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
                false ,
                "§1",
                false,
                date
            ),
            ToDo(
                1,
                "Task 2",
                false ,
                "2",
                false,
                date
            ),
            ToDo(
                2,
                "Task 3",
                false ,
                " 3",
                false,
                date
            )
        )
        return todos
    }
}