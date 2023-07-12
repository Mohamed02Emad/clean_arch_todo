package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertUpdateTodoUseCase(private val repository: BaseRepositoryImpl){
    suspend operator fun invoke(todo: ToDo) = withContext(Dispatchers.IO) {
         repository.dao.insertAndUpdateTodo(todo)
    }
}