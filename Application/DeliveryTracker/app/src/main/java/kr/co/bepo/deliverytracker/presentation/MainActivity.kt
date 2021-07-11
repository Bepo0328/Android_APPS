package kr.co.bepo.deliverytracker.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import kr.co.bepo.deliverytracker.R
import kr.co.bepo.deliverytracker.databinding.ActivityMainBinding
import kr.co.bepo.deliverytracker.work.TrackingCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initWorker()
    }

    private fun initViews() {
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
        binding.toolbar.setupWithNavController(navigationController)
    }

    private fun initWorker() {
        val workerStartTime = Calendar.getInstance()
        workerStartTime.set(Calendar.HOUR_OF_DAY, 16)
        val initialDelay = workerStartTime.timeInMillis - System.currentTimeMillis()
        val dailyTrackingCheckRequest =
            PeriodicWorkRequestBuilder<TrackingCheckWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "DailyTrackingCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                dailyTrackingCheckRequest
            )
    }
}