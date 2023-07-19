package com.motodo.todo.domain.repositories

import com.motodo.todo.domain.models.ToDo

interface BaseRepository {
    suspend fun getListForDate(year: String, month: String, day: String): List<ToDo>

    suspend fun insertUpdateToDo(todo: ToDo)

    fun getAllPreviousTodos(): List<ToDo>

    suspend fun deleteToDo(todo: ToDo)

    suspend fun updateTodos(todo: ToDo)

    fun setAlarm(todo: ToDo)

}