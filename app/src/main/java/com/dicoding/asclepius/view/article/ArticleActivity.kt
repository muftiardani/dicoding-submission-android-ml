package com.dicoding.asclepius.view.article

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityArticleBinding
import com.dicoding.asclepius.helper.BottomNavigationHelper

class ArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleBinding
    private val articleViewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ArticleActivity", "onCreate dipanggil")
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.apply {
            navBottom.selectedItemId = R.id.article
            BottomNavigationHelper.setupBottomNavigation(this@ArticleActivity, navBottom)
            rvArticles.layoutManager = LinearLayoutManager(this@ArticleActivity)
        }
    }

    private fun observeViewModel() {
        articleViewModel.apply {
            listArticle.observe(this@ArticleActivity) { articles ->
                setArticle(articles)
            }
            isLoading.observe(this@ArticleActivity) { loading ->
                showLoading(loading)
            }
            isError.observe(this@ArticleActivity) { error ->
                showError(error)
            }
        }
    }

    private fun setArticle(listArticle: List<ArticlesItem>) {
        val adapter = ArticleAdapter(this)
        adapter.submitList(listArticle)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(this, "Artikel tidak bisa didapatkan", Toast.LENGTH_SHORT).show()
        }
    }
}