package com.motodo.todo.domain.repositories

import androidx.lifecycle.LiveData
import com.motodo.todo.domain.models.ToDo
import java.util.Date

interface BaseRepository {
    suspend fun getListForDate(year : String , month : String , day :String ): List<ToDo>

    suspend fun insertUpdateToDo(todo: ToDo)

    suspend fun deleteToDo(todo: ToDo)

}