package com.motodo.todo.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.motodo.todo.domain.models.ToDo

@Database(entities = [ToDo::class], version = 1)
abstract class TodoDataBase : RoomDatabase() {

    abstract fun myDao(): TodoDao

    companion object {
        private var instancee: TodoDataBase? = null

        private const val DB_NAME = "todo_database"

        fun getInstance(context: Context): TodoDataBase {

            return instancee ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    TodoDataBase::class.java,
                    DB_NAME
                ).build()
                instancee = instance
                instance
            }
        }
    }
}