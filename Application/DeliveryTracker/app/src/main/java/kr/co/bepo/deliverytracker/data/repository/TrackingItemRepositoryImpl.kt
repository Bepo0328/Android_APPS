package kr.co.bepo.deliverytracker.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kr.co.bepo.deliverytracker.data.api.SweetTrackerApi
import kr.co.bepo.deliverytracker.data.db.TrackingItemDao
import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem

class TrackingItemRepositoryImpl(
    private val trackingApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> =
        trackingItemDao.allTrackingItems()
            .distinctUntilChanged()
            .flowOn(dispatcher)

    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> = withContext(dispatcher) {
        trackingItemDao.getAll()
            .mapNotNull { trackingItem ->
                val relatedTrackingInfo = trackingApi.getTrackingInformation(
                    companyCode = trackingItem.company.code,
                    invoice = trackingItem.invoice
                ).body()?.sortTrackingDetailsByTimeDescending()

                if (!relatedTrackingInfo!!.errorMessage.isNullOrBlank()) {
                    null
                } else {
                    trackingItem to relatedTrackingInfo
                }
            }
            .sortedWith(
                compareBy(
                    { it.second.level },
                    { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) }
                )
            )
    }

    override suspend fun getTrackingInformation(companyCode: String, invoice: String): TrackingInformation? =
        trackingApi.getTrackingInformation(companyCode, invoice)
            .body()
            ?.sortTrackingDetailsByTimeDescending()

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) {
        val trackingInformation = trackingApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if (!trackingInformation!!.errorMessage.isNullOrBlank()) {
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) {
        trackingItemDao.delete(trackingItem)
    }

    private fun TrackingInformation.sortTrackingDetailsByTimeDescending() =
        copy(trackingDetails = trackingDetails?.sortedByDescending { it.time ?: 0L })
}