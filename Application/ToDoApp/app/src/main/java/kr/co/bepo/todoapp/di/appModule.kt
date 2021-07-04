package kr.co.bepo.todoapp.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kr.co.bepo.todoapp.data.local.db.ToDoDatabase
import kr.co.bepo.todoapp.data.repository.DefaultToDoRepository
import kr.co.bepo.todoapp.data.repository.ToDoRepository
import kr.co.bepo.todoapp.domain.todo.*
import kr.co.bepo.todoapp.presentation.detail.DetailMode
import kr.co.bepo.todoapp.presentation.detail.DetailViewModel
import kr.co.bepo.todoapp.presentation.list.ListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

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
    single<ToDoRepository> { DefaultToDoRepository(get(), get()) }

    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }
}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()