package kr.co.bepo.deliverytracker

import android.app.Application
import androidx.work.Configuration
import kr.co.bepo.deliverytracker.di.appModule
import kr.co.bepo.deliverytracker.work.AppWorkerFactory
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application(), Configuration.Provider {

    private val workFactory: AppWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) Level.DEBUG
                else Level.NONE
            )
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) android.util.Log.DEBUG
                else android.util.Log.INFO
            )
            .setWorkerFactory(workFactory)
            .build()
}