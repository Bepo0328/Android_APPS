package kr.co.bepo.todoapp.domain.todo

import kr.co.bepo.todoapp.data.entity.ToDoEntity
import kr.co.bepo.todoapp.data.repository.ToDoRepository
import kr.co.bepo.todoapp.domain.UseCase

internal class GetToDoItemUseCase(
    private val toDoRepository: ToDoRepository
) : UseCase {

    suspend operator fun invoke(itemId: Long): ToDoEntity? {
        return toDoRepository.getToDoItem(itemId)
    }
}