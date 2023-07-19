package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.utils.isOldDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetAlarmForTodoUseCase (private val repository: BaseRepositoryImpl) {
    suspend operator fun invoke(todo:ToDo)= withContext(Dispatchers.IO) {
        repository.setAlarm(todo)
    }

}