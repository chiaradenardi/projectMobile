package com.example.projectmobile.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["username"],
        childColumns = ["username"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["username"])]
)
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String, // FK a User
    val activityId: Long, // FK a Activity
    val bookingDate: Long // Timestamp della data di prenotazione
)
