package kr.co.bepo.shoppingapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.co.bepo.shoppingapp.data.db.dao.ProductDao
import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity
import kr.co.bepo.shoppingapp.utillity.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class ProductDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ProductDataBase.db"
    }

    abstract fun productDao(): ProductDao
}