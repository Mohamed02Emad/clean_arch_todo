package com.motodo.todo.data.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.motodo.todo.domain.models.ToDo
import java.util.Date

@Dao
interface TodoDao {
    @Query("select * from ToDo where date = :date")
    fun getTodosByDate(date: Date) : LiveData<List<ToDo>>
    @Delete
    fun deleteTodo(todo: ToDo)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndUpdateTodo(todo: ToDo)
}