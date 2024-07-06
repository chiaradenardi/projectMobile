package com.example.projectmobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertActivity(activity: Activity)

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun getActivityById(id: Long): Activity?

    @Query("SELECT * FROM activities")
    suspend fun getAllActivities(): List<Activity>
}

