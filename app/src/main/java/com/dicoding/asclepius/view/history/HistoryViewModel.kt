package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class HistoryViewModel(application: Application) : ViewModel() {

    private val historyRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistory() : LiveData<List<History>> = historyRepository.getAllHistory()
}