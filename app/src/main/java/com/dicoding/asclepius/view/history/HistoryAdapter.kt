package com.dicoding.asclepius.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.databinding.ItemRowHistoryBinding

class HistoryAdapter(
    private val historyList: List<History>,
    private val historyRepository: HistoryRepository
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    class ViewHolder(var binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyList = historyList[position]
        Glide.with(holder.itemView.context)
            .load(historyList.image)
            .into(holder.binding.imageHistory)
        holder.apply {
            binding.apply {
                tvPrediction.text = historyList.prediction
                tvScore.text = historyList.score
                btnDelete.setOnClickListener{
                    historyRepository.delete(historyList)
                }
            }
        }
    }
}