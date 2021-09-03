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

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var database: AppDatabase

    private var model: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = getDatabase(this)
        model = intent.getParcelableExtra("bookModel")

        detailView()
        saveButton()

    }

    private fun saveButton() {
        binding.saveButton.setOnClickListener {
            Thread {
                database.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }

    private fun detailView() {

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverLargeUrl.orEmpty())
            .into(binding.coverImageView)

        // 저장되어있는 리뷰 불러오기
        Thread {
            val review = database.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            review?.let {
                runOnUiThread {
                    binding.reviewEditText.setText(it.review)
                }
            }
        }
        /*Thread {
            val review = database.reviewDao().registerReview(bookDetail?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }*/
    }


}