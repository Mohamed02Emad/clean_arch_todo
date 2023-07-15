package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.utils.isOldDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPreviousTodosUseCase(private val repository: BaseRepositoryImpl) {
    suspend operator fun invoke(): ArrayList<ToDo> = withContext(Dispatchers.IO) {
        val arr = ArrayList<ToDo>()
        arr.addAll(
            repository.dao.getAllPreviousTodos()
                .filter { isOldDate(it.day.toInt(), it.month.toInt(), it.year.toInt())}
                .sortedByDescending{it.id}
        )
        arr
    }

}