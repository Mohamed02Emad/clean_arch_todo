package com.motodo.todo.domain.repositories

import androidx.lifecycle.LiveData
import com.motodo.todo.domain.models.ToDo
import java.util.Date

interface BaseRepository {
    suspend fun getListForDate(date: Date): LiveData<List<ToDo>>

    suspend fun insertToDo(todo: ToDo)

    suspend fun deleteToDo(todo: ToDo)

}