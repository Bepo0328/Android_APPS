package kr.co.bepo.calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.co.bepo.calculator.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Delete
    fun delete(history: History)

    @Query("SELECT * FROM history WHERE result LIKE :result")
    fun findByResult(result: String): History
}