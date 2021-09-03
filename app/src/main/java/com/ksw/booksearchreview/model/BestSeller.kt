package com.ksw.booksearchreview.model

import com.google.gson.annotations.SerializedName

/**
 * Created by KSW on 2021-09-03
 */

data class BestSeller(
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)
