package com.motodo.todo.domain.useCases

import androidx.lifecycle.LiveData
import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import java.util.Date

class DeleteTodoUseCase(private val repository: BaseRepositoryImpl)  {
    suspend operator fun invoke(todo: ToDo) {
        return repository.dao.deleteTodo(todo)
    }
}