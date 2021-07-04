package kr.co.bepo.todoapp

import android.app.Application
import kr.co.bepo.todoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ToDoAppApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO Koin Trigger
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ToDoAppApplication)
            modules(appModule)
        }
    }
}