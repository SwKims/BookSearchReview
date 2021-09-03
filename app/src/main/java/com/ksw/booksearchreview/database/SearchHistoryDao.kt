package com.ksw.booksearchreview.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ksw.booksearchreview.model.SearchHistory

/**
 * Created by KSW on 2021-09-02
 */

@Dao
interface SearchHistoryDao {
    // 전부 가져옴
    @Query("SELECT * FROM history")
    fun getAll(): List<SearchHistory>

    // 검색 작업이 일어날 때 insert
    @Insert
    fun insertHistory(history: SearchHistory)

    // x 눌렀을 때 키워드 지워준다
    @Query("DELETE FROM history WHERE keyword = :keyword")
    fun delete(keyword: String)
}