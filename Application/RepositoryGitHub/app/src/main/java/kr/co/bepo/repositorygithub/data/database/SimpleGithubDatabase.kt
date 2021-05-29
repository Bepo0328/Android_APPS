package kr.co.bepo.repositorygithub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.bepo.repositorygithub.data.dao.SearchHistoryDao
import kr.co.bepo.repositorygithub.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase : RoomDatabase() {
    abstract fun repositoryDao(): SearchHistoryDao
}