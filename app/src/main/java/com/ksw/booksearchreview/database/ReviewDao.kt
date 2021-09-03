package com.ksw.booksearchreview.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ksw.booksearchreview.model.Review

/**
 * Created by KSW on 2021-09-02
 */

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id = :id")
    fun getOneReview(id : Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)

}