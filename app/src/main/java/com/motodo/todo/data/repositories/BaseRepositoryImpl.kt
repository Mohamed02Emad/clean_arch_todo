package com.motodo.todo.data.repositories

import com.motodo.todo.data.source.TodoDao
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.repositories.BaseRepository
import java.util.Date

class BaseRepositoryImpl(val dao : TodoDao) : BaseRepository{
    override suspend fun getListForDate(year : String , month :String , day:String): List<ToDo> = dao.getTodosByDate(year, month, day)

    override suspend fun insertUpdateToDo(todo: ToDo) = dao.insertAndUpdateTodo(todo)

    override suspend fun deleteToDo(todo: ToDo) = dao.deleteTodo(todo)

}