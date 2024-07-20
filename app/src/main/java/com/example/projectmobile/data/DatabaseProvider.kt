package com.example.projectmobile.data

import android.content.Context

object DatabaseProvider {

    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: AppDatabase.getInstance(context).also { instance = it }
        }
    }

    fun getUserDao(context: Context): UserDao {
        return AppDatabase.getInstance(context).userDao()
    }
}
