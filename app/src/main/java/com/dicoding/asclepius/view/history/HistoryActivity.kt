package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.BottomNavigationHelper

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var rvHistory: RecyclerView
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBottom.selectedItemId = R.id.history
        BottomNavigationHelper.setupBottomNavigation(this, binding.navBottom)

        val viewModelFactory = ViewModelFactory(this.application)
        historyViewModel =
            ViewModelProvider(this, viewModelFactory)[HistoryViewModel::class.java]

        rvHistory = binding.rvHistory

        historyViewModel.getAllHistory().observe(this) { history ->
            if (history.isEmpty()) {
                binding.rvHistory.visibility = View.GONE
                Toast.makeText(this, "History is empty", Toast.LENGTH_SHORT).show()
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                showRecyclerView(history)
            }
        }
    }

    private fun showRecyclerView(history: List<History>) {
        rvHistory.layoutManager = LinearLayoutManager(this)
        val historyAdapter = HistoryAdapter(history, HistoryRepository(this.application))
        rvHistory.adapter = historyAdapter
    }
}