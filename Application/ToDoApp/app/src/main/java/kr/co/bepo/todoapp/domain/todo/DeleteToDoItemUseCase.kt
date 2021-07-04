package kr.co.bepo.todoapp.domain.todo

import kr.co.bepo.todoapp.data.repository.ToDoRepository
import kr.co.bepo.todoapp.domain.UseCase

internal class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(id: Long) {
        return toDoRepository.deleteToDoItem(id)
    }
}