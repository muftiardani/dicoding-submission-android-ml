package com.dicoding.asclepius.view.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig.API_KEY
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.DetailNewsResponse
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val _listArticle = MutableLiveData<List<ArticlesItem>>()
    val listArticle: LiveData<List<ArticlesItem>> = _listArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        _isLoading.value = true
        ApiConfig.getApiService().getTopHeadlines(QUERY, CATEGORY, LANGUAGE, API_KEY)
            .enqueue(object : Callback<DetailNewsResponse> {
                override fun onResponse(
                    call: Call<DetailNewsResponse>,
                    response: Response<DetailNewsResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        _isError.value = false
                        _listArticle.value = response.body()?.articles
                    } else {
                        handleError("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailNewsResponse>, t: Throwable) {
                    handleError(t.message ?: "Unknown error occurred")
                }
            })
    }

    private fun handleError(errorMessage: String) {
        _isLoading.value = false
        _isError.value = true
        Log.e(TAG, "Error: $errorMessage")
    }

    companion object {
        private const val TAG = "ArticleViewModel"
        private const val QUERY = "cancer"
        private const val CATEGORY = "health"
        private const val LANGUAGE = "en"
    }
}