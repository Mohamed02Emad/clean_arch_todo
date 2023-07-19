package com.motodo.todo.data.repositories

import android.content.Context
import com.motodo.todo.data.receivers.todosAlarm.TodosAlarmReceiver
import com.motodo.todo.data.source.TodoDao
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.repositories.BaseRepository

class BaseRepositoryImpl(val dao : TodoDao , val context:Context) : BaseRepository{
    override suspend fun getListForDate(year : String , month :String , day:String): List<ToDo> = dao.getTodosByDate(year, month, day)
    override suspend fun insertUpdateToDo(todo: ToDo) = dao.insertAndUpdateTodo(todo)
    override fun getAllPreviousTodos(): List<ToDo> = dao.getAllTodos()
    override suspend fun deleteToDo(todo: ToDo) = dao.deleteTodo(todo)
    override suspend fun updateTodos(todo: ToDo) {
        dao.updateTodo(todo)
    }

    override fun setAlarm( todo: ToDo) {
        TodosAlarmReceiver.setTodoAlarm(context, todo)
    }


}