package com.motodo.todo.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.motodo.todo.data.source.TodoDao
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.repositories.BaseRepository
import java.util.Date

class BaseRepositoryImpl(dao : TodoDao) : BaseRepository{

    override suspend fun getListForDate(date: Date): LiveData<List<ToDo>> {
        val todos  = MutableLiveData<List<ToDo>>()
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

    override suspend fun insertToDo(todo: ToDo) {
    }

    override suspend fun deleteToDo(todo: ToDo) {
    }
}