package com.ksw.booksearchreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ksw.booksearchreview.database.AppDatabase
import com.ksw.booksearchreview.database.getDatabase
import com.ksw.booksearchreview.databinding.ActivityDetailBinding
import com.ksw.booksearchreview.model.Book
import com.ksw.booksearchreview.model.Review

/**
 * Created by KSW on 2021-09-03
 */

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var database: AppDatabase

    private var bookDetail: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = getDatabase(this)
        bookDetail = intent.getParcelableExtra("bookModel")

        detailView()
        saveButton()

    }

    private fun detailView() {

        binding.titleTextView.text = bookDetail?.title.orEmpty()
        binding.descriptionTextView.text = bookDetail?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(bookDetail?.coverLargeUrl.orEmpty())
            .into(binding.coverImageView)

        // 저장되어있는 리뷰 불러오기
        Thread {
            val review = database.reviewDao().registerReview(bookDetail?.id?.toInt() ?: 0)
            review?.let {
                runOnUiThread {
                    binding.reviewEditText.setText(it.review)
                }
            }
        }
    }

    private fun saveButton() {
        binding.saveButton.setOnClickListener {
            Thread {
                database.reviewDao().saveReview(
                    Review(
                        bookDetail?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }



}