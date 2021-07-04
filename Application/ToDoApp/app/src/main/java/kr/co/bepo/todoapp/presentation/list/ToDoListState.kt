package kr.co.bepo.todoapp.presentation.list

import kr.co.bepo.todoapp.data.entity.ToDoEntity

sealed class ToDoListState {

    object UnInitialized : ToDoListState()

    object Loading : ToDoListState()

    data class Success(
        val toDoList: List<ToDoEntity>
    ) : ToDoListState()

    object Error : ToDoListState()
}