package com.dicoding.asclepius.view.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> = _currentImageUri

    private val _uiState = MutableLiveData<ImageUiState>(ImageUiState.Initial)
    val uiState: LiveData<ImageUiState> = _uiState

    fun setImageUri(uri: Uri) {
        viewModelScope.launch {
            try {
                _uiState.value = ImageUiState.Loading
                _currentImageUri.value = uri
                _uiState.value = ImageUiState.Success(uri)
            } catch (e: Exception) {
                _uiState.value = ImageUiState.Error(e.message ?: "Unknown error occurred")
                _currentImageUri.value = null
            }
        }
    }

    fun clearImage() {
        _currentImageUri.value = null
        _uiState.value = ImageUiState.Initial
    }
}

sealed class ImageUiState {
    object Initial : ImageUiState()
    object Loading : ImageUiState()
    data class Success(val uri: Uri) : ImageUiState()
    data class Error(val message: String) : ImageUiState()
}