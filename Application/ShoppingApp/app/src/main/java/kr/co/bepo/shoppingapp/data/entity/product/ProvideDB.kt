package kr.co.bepo.shoppingapp.data.entity.product

import android.content.Context
import androidx.room.Room
import kr.co.bepo.shoppingapp.data.db.ProductDatabase

internal fun provideDB(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ProductDatabase) = database.productDao()