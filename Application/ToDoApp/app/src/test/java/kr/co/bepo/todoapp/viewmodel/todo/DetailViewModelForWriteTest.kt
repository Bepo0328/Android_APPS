package kr.co.bepo.todoapp.viewmodel.todo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.co.bepo.todoapp.ViewModelTest
import kr.co.bepo.todoapp.data.entity.ToDoEntity
import kr.co.bepo.todoapp.domain.todo.InsertToDoItemUseCase
import kr.co.bepo.todoapp.presentation.detail.DetailMode
import kr.co.bepo.todoapp.presentation.detail.DetailViewModel
import kr.co.bepo.todoapp.presentation.detail.ToDoDetailState
import kr.co.bepo.todoapp.presentation.list.ListViewModel
import kr.co.bepo.todoapp.presentation.list.ToDoListState
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModelTest]을 테스트하기 위한 Unit Test Class
 *
 * 1. test viewModel fetch
 * 2. test insert todo
 */

@ExperimentalCoroutinesApi
internal class DetailViewModelForWriteTest: ViewModelTest() {
    private val id = 0L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Write
            )
        )
    }

    @Test
    fun `test insert todo`() = runBlockingTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()
        val listTestObservable = listViewModel.toDoListLiveData.test()

        detailViewModel.writeToDo(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnInitialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.DETAIL)
        assert(detailViewModel.id == id)

        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf(todo))
            )
        )
    }

}