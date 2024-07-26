package com.example.projectmobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cart: Cart)

    @Query("SELECT * FROM cart WHERE username = :username")
    suspend fun getUserCart(username: String): List<Cart>

    @Query("""
        SELECT activities.* FROM cart
        INNER JOIN activities ON cart.activityId = activities.id
    """)
    suspend fun getCartActivities(): List<Activity>
}

