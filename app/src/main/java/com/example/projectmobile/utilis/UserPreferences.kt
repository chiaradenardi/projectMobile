package com.example.projectmobile.utilis

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USERNAME = "username"
    }

    var userEmail: String?
        get() = sharedPreferences.getString(KEY_USER_EMAIL, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_USER_EMAIL, value).apply()
        }

    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) {
            sharedPreferences.edit().putString(KEY_USERNAME, value).apply()
        }
}
