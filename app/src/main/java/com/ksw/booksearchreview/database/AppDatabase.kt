package com.ksw.booksearchreview.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksw.booksearchreview.model.Review
import com.ksw.booksearchreview.model.SearchHistory

/**
 * Created by KSW on 2021-09-02
 */

@Database(entities = [SearchHistory::class, Review::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao() : SearchHistoryDao
    abstract fun reviewDao() : ReviewDao

}