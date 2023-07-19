package com.motodo.todo.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.motodo.todo.domain.models.ToDo

@Database(entities = [ToDo::class], version = 1)
abstract class TodoDataBase : RoomDatabase() {
   abstract val myDao: TodoDao
}