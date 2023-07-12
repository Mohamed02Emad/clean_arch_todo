package com.motodo.todo.domain.useCases

data class TodoUseCases (
    val deleteTodoUseCase: DeleteTodoUseCase,
    val getTodosUseCase: GetDateToDosUseCase,
    val insertUpdateTodoUseCase: InsertUpdateTodoUseCase
)