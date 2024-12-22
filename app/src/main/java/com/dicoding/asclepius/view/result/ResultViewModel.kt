package com.dicoding.asclepius.view.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.repository.HistoryRepository
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : ViewModel() {
    private val repository = HistoryRepository(application)

    private val _saveStatus = MutableLiveData<SaveStatus>()
    val saveStatus: LiveData<SaveStatus> = _saveStatus

    fun insert(history: History) {
        viewModelScope.launch {
            try {
                _saveStatus.value = SaveStatus.Loading
                repository.insert(history)
                _saveStatus.value = SaveStatus.Success
            } catch (e: Exception) {
                _saveStatus.value = SaveStatus.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class SaveStatus {
    object Loading : SaveStatus()
    object Success : SaveStatus()
    data class Error(val message: String) : SaveStatus()
}