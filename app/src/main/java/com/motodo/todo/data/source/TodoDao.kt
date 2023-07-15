package com.motodo.todo.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.motodo.todo.domain.models.ToDo

@Dao
interface TodoDao {
    @Query("select * from ToDo where year = :year and month = :month and day = :day")
     fun getTodosByDate(day: String , month : String , year : String) : List<ToDo>
    @Query("SELECT * FROM ToDo ")
    fun getAllPreviousTodos(): List<ToDo>

    @Delete
    suspend fun deleteTodo(todo: ToDo)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndUpdateTodo(todo: ToDo)

    @Update
    suspend fun updateTodo(todo: ToDo)
}