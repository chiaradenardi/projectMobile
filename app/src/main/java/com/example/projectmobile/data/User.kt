package com.example.projectmobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "User",
    indices = [Index(value = ["username"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val bio: String? = null,
    val profileImage: String? = null,
    val darkMode: Boolean
)
