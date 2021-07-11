package kr.co.bepo.deliverytracker.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.deliverytracker.data.api.SweetTrackerApi
import kr.co.bepo.deliverytracker.data.db.ShippingCompanyDao
import kr.co.bepo.deliverytracker.data.entity.ShippingCompany
import kr.co.bepo.deliverytracker.data.preference.PreferenceManager

class ShippingCompanyRepositoryImpl(
    private val trackerApi: SweetTrackerApi,
    private val shippingCompanyDao: ShippingCompanyDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS =
            "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        val currentTimeMillis = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMillis =
            preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null ||
            CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdatedTimeMillis)
        ) {
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()
            shippingCompanyDao.insert(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? =
        withContext(dispatcher) {
            try {
                trackerApi.getRecommendShippingCompanies(invoice)
                    .body()
                    ?.shippingCompanies
                    ?.minByOrNull { it.code.toIntOrNull() ?: Int.MAX_VALUE }
            } catch (e: Exception) {
                null
            }
        }
}