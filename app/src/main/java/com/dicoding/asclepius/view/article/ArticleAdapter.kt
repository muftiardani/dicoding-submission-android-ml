package com.dicoding.asclepius.view.article

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemRowArticlesBinding

class ArticleAdapter(private val context: Context) :
    ListAdapter<ArticlesItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowArticlesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        holder.itemView.setOnClickListener {
            handleArticleClick(article)
        }
    }

    override fun submitList(list: List<ArticlesItem>?) {
        val filteredList = list?.filterNot {
            it.title == "[Removed]" || it.title == null
        }
        super.submitList(filteredList)
    }

    private fun handleArticleClick(article: ArticlesItem) {
        Toast.makeText(context, "Membuka ${article.title}", Toast.LENGTH_SHORT).show()

        if (article.url == null) {
            Toast.makeText(context, "Link tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        context.startActivity(intent)
    }

    class ViewHolder(private val binding: ItemRowArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticlesItem) {
            binding.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description
                Glide.with(root)
                    .load(article.urlToImage)
                    .into(imageNews)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}