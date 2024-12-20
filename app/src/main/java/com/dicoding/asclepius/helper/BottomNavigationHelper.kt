package com.dicoding.asclepius.helper

// BottomNavigationHelper.kt
import android.app.Activity
import android.content.Intent
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.main.MainActivity
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.history.HistoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationHelper {

    fun setupBottomNavigation(activity: Activity, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.analyze -> {
                    if (activity !is MainActivity) {
                        activity.startActivity(Intent(activity, MainActivity::class.java))
                        activity.overridePendingTransition(0, 0)
                    }
                    true
                }
                R.id.article -> {
                    if (activity !is ArticleActivity) {
                        activity.startActivity(Intent(activity, ArticleActivity::class.java))
                        activity.overridePendingTransition(0, 0)
                    }
                    true
                }
                R.id.history -> {
                    if (activity !is HistoryActivity) {
                        activity.startActivity(Intent(activity, HistoryActivity::class.java))
                        activity.overridePendingTransition(0, 0)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
