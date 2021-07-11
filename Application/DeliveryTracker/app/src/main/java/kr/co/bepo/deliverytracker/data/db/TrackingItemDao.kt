package kr.co.bepo.deliverytracker.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.co.bepo.deliverytracker.data.entity.TrackingItem

@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems(): Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    suspend fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TrackingItem)

    @Delete
    suspend fun delete(item: TrackingItem)
}