package com.example.projectmobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites WHERE username = :username")
    suspend fun getUserFavorites(username: String): List<Favorite>
}
