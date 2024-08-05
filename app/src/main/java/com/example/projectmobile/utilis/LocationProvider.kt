package com.example.projectmobile.utilis

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import android.location.Location
import kotlinx.coroutines.tasks.await

class LocationProvider(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): Location? {
        return try {
            val locationResult: Task<Location> = fusedLocationClient.lastLocation
            locationResult.await()
        } catch (e: Exception) {
            null
        }
    }
}
