package com.motodo.todo.domain.useCases

import com.motodo.todo.domain.models.Priority
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.utils.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SortTodosUseCase {
    suspend operator fun invoke(arr : ArrayList<ToDo> ,todo: ToDo) : ArrayList<ToDo> = withContext(Dispatchers.IO) {
        arr.add(todo)
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