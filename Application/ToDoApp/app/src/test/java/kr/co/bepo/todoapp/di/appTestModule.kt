package kr.co.bepo.todoapp.di

import kr.co.bepo.todoapp.data.repository.TestToDoRepository
import kr.co.bepo.todoapp.data.repository.ToDoRepository
import kr.co.bepo.todoapp.domain.todo.*
import kr.co.bepo.todoapp.presentation.detail.DetailMode
import kr.co.bepo.todoapp.presentation.detail.DetailViewModel
import kr.co.bepo.todoapp.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoItemUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }

    // Repository
    single<ToDoRepository> { TestToDoRepository() }

}