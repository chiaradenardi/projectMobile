package com.example.projectmobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun getActivityById(id: Long): Activity?

    @Query("SELECT * FROM activities")
    suspend fun getAllActivities(): List<Activity>

    @Query("DELETE FROM activities")
    suspend fun deleteAll()

    @Query("SELECT * FROM activities WHERE category = :category")
    fun getActivitiesByCategory(category: String): List<Activity>
}
