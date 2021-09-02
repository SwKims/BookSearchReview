package com.ksw.booksearchreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by KSW on 2021-09-02
 */

@Entity(tableName = "history")
data class SearchHistory(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "keyword") val keyword: String?
)