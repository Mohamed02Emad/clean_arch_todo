package com.motodo.todo.domain.useCases

data class TodoUseCases (
    val deleteTodoUseCase: DeleteTodoUseCase,
    val getTodosUseCase: GetDateToDosUseCase,
    val insertTodoUseCase: InsertTodoUseCase,
    val getPreviousTodosUseCase: GetPreviousTodosUseCase,
    val updateTodoUseCase: UpdateTodoUseCase,
    val setAlarmForTodoUseCase: SetAlarmForTodoUseCase
)