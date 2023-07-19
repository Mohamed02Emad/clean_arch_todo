package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateTodoUseCase (val repositoryImpl: BaseRepositoryImpl){
    suspend operator fun invoke(todo: ToDo) = withContext(Dispatchers.IO) {
        repositoryImpl.dao.updateTodo(todo)
    }
}