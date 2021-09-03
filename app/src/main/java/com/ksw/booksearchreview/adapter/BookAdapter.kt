package com.ksw.booksearchreview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksw.booksearchreview.R
import com.ksw.booksearchreview.databinding.ItemBooksBinding
import com.ksw.booksearchreview.model.Book

/**
 * Created by KSW on 2021-09-03
 */

class BookAdapter(private val itemClickListener: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookHolder>(diffUtil) {

    inner class BookHolder(private val binding: ItemBooksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemBook: Book) {
            binding.BookTitle.text = itemBook.title
            binding.BookSummary.text = itemBook.description

            binding.root.setOnClickListener {
                itemClickListener(itemBook)
            }

            Glide
                .with(binding.ImageBook.context)
                .load(itemBook.coverSmallUrl)
                .into(binding.ImageBook)

        }
    }

    // 만들어진 뷰 홀더가 없을 경우 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.item_books, parent, false)
        return BookHolder(
            ItemBooksBinding.bind(view)
        )
    }

    // 뷰 홀더가 뷰에 그려질 경우 데이터를 바인딩 하는 함수.
    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}