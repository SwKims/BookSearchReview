package com.ksw.booksearchreview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ksw.booksearchreview.model.Review
import com.ksw.booksearchreview.model.SearchHistory

/**
 * Created by KSW on 2021-09-02
 */

@Database(entities = [SearchHistory::class, Review::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun reviewDao(): ReviewDao

}

fun getDatabase(context: Context): AppDatabase {

    val migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` ('id' INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
        }
    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration)
        .build()

}