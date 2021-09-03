package com.ksw.booksearchreview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksw.booksearchreview.adapter.BookAdapter
import com.ksw.booksearchreview.adapter.HistoryAdapter
import com.ksw.booksearchreview.database.AppDatabase
import com.ksw.booksearchreview.database.getDatabase
import com.ksw.booksearchreview.databinding.ActivityMainBinding
import com.ksw.booksearchreview.model.BestSeller
import com.ksw.booksearchreview.model.SearchBook
import com.ksw.booksearchreview.model.SearchHistory
import com.ksw.booksearchreview.network.BookServiceApi
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
    private lateinit var historyAdapter: HistoryAdapter

    private val database: AppDatabase by lazy {
        getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        startService()
        bookServiceLoadBestSellers()
    }

    private fun bookServiceLoadBestSellers() {
        bookServiceApi.getBestSellerBooks(API_KEY)
            .enqueue(object : Callback<BestSeller> {
                override fun onResponse(call: Call<BestSeller>, response: Response<BestSeller>) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "onResponse")
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, "onResponse")
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        bookAdapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSeller>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }


    private fun initBookRecyclerView() {
        bookAdapter = BookAdapter(itemClickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        binding.booksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.booksRecyclerView.adapter = bookAdapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        }, this)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

        initSearchEditText()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                booksBestSeller(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.searchEditText.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun deleteSearchKeyword(keyWord: String) {
        Thread {
            database.searchHistoryDao().delete(keyWord)
            showHistoryView()
        }.start()
    }

    private fun showHistoryView() {
        Thread {
            val keyWords = database.searchHistoryDao().getAll().reversed()
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keyWords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    fun booksBestSeller(keyword: String) {
        bookServiceApi.getSearchBookName(API_KEY, keyword)
            .enqueue(object : Callback<SearchBook> {
                override fun onResponse(call: Call<SearchBook>, response: Response<SearchBook>) {

                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    bookAdapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBook>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, "onFailure ")
                }
            })
    }

    private fun saveSearchKeyword(keyWord: String) {
        Thread {
            database.searchHistoryDao().insertHistory(SearchHistory(null, keyWord))
        }.start()
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