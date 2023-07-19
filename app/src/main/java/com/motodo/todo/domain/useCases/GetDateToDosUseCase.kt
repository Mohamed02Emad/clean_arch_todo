package com.motodo.todo.domain.useCases

import com.motodo.todo.data.repositories.BaseRepositoryImpl
import com.motodo.todo.domain.models.Priority
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.utils.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.time.days

class GetDateToDosUseCase(private val repository: BaseRepositoryImpl) {
    suspend operator fun invoke(date: Date): ArrayList<ToDo> = withContext(Dispatchers.IO) {
        val arr = ArrayList<ToDo>()

        arr.addAll(repository.dao.getTodosByDate(DateHelper.getDay(date),DateHelper.getMonthIndex(date) ,DateHelper.getYearName(date)))
        sortArrayByPriority(arr)
    }

    private fun sortArrayByPriority(arr: ArrayList<ToDo>): ArrayList<ToDo> {
        val high = arr.filter { it.priority == Priority.HIGH }
        val medium = arr.filter { it.priority == Priority.MEDIUM }
        val low = arr.filter { it.priority == Priority.LOW }
        val none = arr.filter { it.priority == Priority.NONE }

        val arrToReturn = ArrayList<ToDo>()
        arrToReturn.addAll(high.sortedBy { it.title })
        arrToReturn.addAll(medium.sortedBy { it.title })
        arrToReturn.addAll(low.sortedBy { it.title })
        arrToReturn.addAll(none.sortedBy { it.title })

        return arrToReturn
    }
}