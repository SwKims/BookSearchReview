package com.ksw.booksearchreview.model

import com.google.gson.annotations.SerializedName

/**
 * Created by KSW on 2021-09-02
 */

data class SearchBook(
    @SerializedName("title") val title : String,
    @SerializedName("item") val books : List<Book>
)
