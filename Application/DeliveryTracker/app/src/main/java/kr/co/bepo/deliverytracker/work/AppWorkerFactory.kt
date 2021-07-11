package kr.co.bepo.deliverytracker.work

import androidx.work.DelegatingWorkerFactory
import kotlinx.coroutines.CoroutineDispatcher
import kr.co.bepo.deliverytracker.data.repository.TrackingItemRepository

class AppWorkerFactory(
    trackingItemRepository: TrackingItemRepository,
    dispatcher: CoroutineDispatcher
) : DelegatingWorkerFactory() {

    init {
        addFactory(TrackingCheckWorkerFactory(trackingItemRepository, dispatcher))
    }
}