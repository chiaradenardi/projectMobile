package com.example.projectmobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val date: Long, // Utilizza un timestamp per rappresentare la data
    val imageUrl: String?,
    val latitude: Double, // Latitudine per la mappa
    val longitude: Double // Longitudine per la mappa
)
