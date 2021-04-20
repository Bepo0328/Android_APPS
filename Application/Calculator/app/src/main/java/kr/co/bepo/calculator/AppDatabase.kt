package kr.co.bepo.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.bepo.calculator.dao.HistoryDao
import kr.co.bepo.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}