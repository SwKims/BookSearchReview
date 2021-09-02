package com.ksw.booksearchreview.network

import com.ksw.booksearchreview.model.SearchBook
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by KSW on 2021-09-02
 */

interface BookServiceApi {

    @GET("/api/search.api?.output=json")
    fun getSearchBookName(
        @Query("key") apiKey: String,
        @Query("query") keyWord: String
    ): Call<SearchBook>
}