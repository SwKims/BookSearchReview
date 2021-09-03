package com.ksw.booksearchreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ksw.booksearchreview.adapter.BookAdapter
import com.ksw.booksearchreview.databinding.ActivityMainBinding
import com.ksw.booksearchreview.model.BestSeller
import com.ksw.booksearchreview.network.BookServiceApi
import com.ksw.booksearchreview.network.NetworkModule
import com.ksw.booksearchreview.util.Constant.Companion.API_KEY
import com.ksw.booksearchreview.util.Constant.Companion.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookServiceApi: BookServiceApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        booksBestSeller()
        startService()


    }

    private fun booksBestSeller() {
        bookServiceApi.getBestSellerBooks(API_KEY)
            .enqueue(object : Callback<BestSeller>{
                override fun onResponse(call: Call<BestSeller>, response: Response<BestSeller>) {
                    if (response.isSuccessful.not()) {
                        Log.d(TAG, "onResponse: ")
                        return
                    }

                    response.body()?.let {
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        bookAdapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSeller>, t: Throwable) {
                    Log.e(TAG, "onFailure " )

                }

            })
    }

    private fun startService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookServiceApi = retrofit.create(BookServiceApi::class.java)
    }


    companion object {
        private const val TAG = "MainActivity"
    }

}