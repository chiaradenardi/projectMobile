package com.example.projectmobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("""
        SELECT activity.* FROM favorites
        INNER JOIN activities AS activity ON favorites.activityId = activity.id
        WHERE favorites.username = :username
    """)
    suspend fun getUserFavoriteActivities(username: String): List<Activity>
}

