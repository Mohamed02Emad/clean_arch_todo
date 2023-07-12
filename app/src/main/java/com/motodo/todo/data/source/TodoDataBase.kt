package com.motodo.todo.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.motodo.todo.data.source.utils.DateConverter
import com.motodo.todo.domain.models.ToDo

@TypeConverters(DateConverter::class)
@Database(entities = [ToDo::class], version = 1)
abstract class TodoDataBase : RoomDatabase() {
   abstract val myDao: TodoDao
}