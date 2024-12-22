package com.dicoding.asclepius.helper

import android.app.Activity
import android.content.Intent
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.main.MainActivity
import com.dicoding.asclepius.view.article.ArticleActivity
import com.dicoding.asclepius.view.history.HistoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationHelper {
    fun setupBottomNavigation(activity: Activity, bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            handleNavigationItemSelected(activity, menuItem.itemId)
        }
    }

    private fun handleNavigationItemSelected(activity: Activity, itemId: Int): Boolean {
        val targetActivity = when (itemId) {
            R.id.analyze -> MainActivity::class.java
            R.id.article -> ArticleActivity::class.java
            R.id.history -> HistoryActivity::class.java
            else -> return false
        }

        if (!isCurrentActivity(activity, targetActivity)) {
            navigateToActivity(activity, targetActivity)
        }

        return true
    }

    private fun isCurrentActivity(activity: Activity, targetActivity: Class<out Activity>): Boolean {
        return activity::class.java == targetActivity
    }

    private fun navigateToActivity(activity: Activity, targetActivity: Class<out Activity>) {
        activity.apply {
            startActivity(Intent(this, targetActivity))
            overridePendingTransition(0, 0)
        }
    }
}