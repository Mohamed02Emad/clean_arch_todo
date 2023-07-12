package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class GetDateToDosUseCase(private val repository: BaseRepositoryImpl) {
    suspend operator fun invoke(date: Date): ArrayList<ToDo> = withContext(Dispatchers.IO){
        val arr = ArrayList<ToDo>()
        arr.addAll(repository.dao.getTodosByDate(date.year,date.month,date.day))
        arr
    }
}