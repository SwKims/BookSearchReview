package com.ksw.booksearchreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ksw.booksearchreview.MainActivity
import com.ksw.booksearchreview.databinding.ItemHistorysearchBinding
import com.ksw.booksearchreview.model.SearchHistory


/**
 * Created by KSW on 2021-09-03
 */

class HistoryAdapter(val historyDeleteClickListener: (String) -> Unit, val mainActivity: MainActivity) :
    ListAdapter<SearchHistory, HistoryAdapter.HistoryViewHolder>(diffUtil) {

    inner class HistoryViewHolder(private val binding: ItemHistorysearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: SearchHistory) {
            binding.tvHistoryKeyword.text = historyModel.keyword
            binding.btHistoryDelete.setOnClickListener {
                historyDeleteClickListener(historyModel.keyword.orEmpty())
            }
            binding.root.setOnClickListener {

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistorysearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SearchHistory>() {
            override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory) =
                oldItem.keyword == newItem.keyword
        }
    }

}