package com.dicoding.asclepius.view.article

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ActivityArticleBinding
import com.dicoding.asclepius.helper.BottomNavigationHelper

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var rvArticle: RecyclerView

    private val ArticleViewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ArticleActivity", "onCreate dipanggil")
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBottom.selectedItemId = R.id.article
        BottomNavigationHelper.setupBottomNavigation(this, binding.navBottom)
        rvArticle = binding.rvArticles

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticles.layoutManager = layoutManager

        ArticleViewModel.listArticle.observe(this) { listArticle ->
            setArticle(listArticle, this)
        }

        ArticleViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        ArticleViewModel.isError.observe(this) {
            showError(it)
        }
    }

    private fun setArticle(listArticle: List<ArticlesItem>, context: Context) {
        val adapter = ArticleAdapter(context)
        adapter.submitList(listArticle)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) Toast.makeText(
            this,
            "Artikel tidak bisa didapatkan",
            Toast.LENGTH_SHORT
        ).show()
    }
}
