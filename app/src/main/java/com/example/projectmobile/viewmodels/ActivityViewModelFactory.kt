package com.example.projectmobile.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectmobile.data.ActivityDao
import com.example.projectmobile.data.FavoriteDao
import com.example.projectmobile.data.UserDao

class ActivityViewModelFactory(
    private val context: Context,
    private val activityDao: ActivityDao,
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(activityDao, userDao, favoriteDao, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
