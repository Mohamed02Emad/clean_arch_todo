package com.motodo.todo.presentation.fragmentPrevious

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.domain.useCases.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PreviousViewModel @Inject constructor(val useCases: TodoUseCases) : ViewModel() {


    private val _todos = MutableLiveData<ArrayList<ToDo>?>(ArrayList())
    val todos: LiveData<ArrayList<ToDo>?> = _todos

    private val _removedTodo = MutableLiveData<ToDo?>(null)
    val removedTodo: LiveData<ToDo?> = _removedTodo


    init {
        viewModelScope.launch {
            getPreviousTodos()
        }
    }

    private suspend fun getPreviousTodos() = withContext(Dispatchers.Main) {
        _todos.value = useCases.getPreviousTodosUseCase()
    }

    suspend fun deleteTodo(todo: ToDo) {
        useCases.deleteTodoUseCase(todo)
        getPreviousTodos()
        withContext(Dispatchers.Main) {
            _removedTodo.value = todo
        }
    }

    suspend fun insertTodo(todo: ToDo) = useCases.insertTodoUseCase(todo)
    suspend fun updateTodo(todo: ToDo , index: Int) {
        useCases.updateTodoUseCase(todo)
        _todos.value?.set(index, todo)
    }


    suspend fun undoDeleting() = withContext(Dispatchers.Main){
        removedTodo.value?.let { todo ->
            insertTodo(todo)
            getPreviousTodos()
            _removedTodo.value = null
        }
    }

}