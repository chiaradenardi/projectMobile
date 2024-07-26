package com.example.projectmobile.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmobile.data.Activity
import com.example.projectmobile.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActivityViewModel(context: Context) : ViewModel() {
    private val db = AppDatabase.getInstance(context)
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    init {
        viewModelScope.launch {
            addPredefinedActivities()
            _activities.value = db.activityDao().getAllActivities()
        }
    }

    private suspend fun addPredefinedActivities() {
        val predefinedActivities = listOf(
            Activity(
                name = "Tour della città",
                description = "Esplora i luoghi storici della città.",
                price = 50.0,
                date = convertDateToTimestamp("2024-08-01"),
                imageUrl = "https://example.com/tour_citta.jpg"
            ),
            Activity(
                name = "Degustazione di vini",
                description = "Prova una selezione dei migliori vini locali.",
                price = 70.0,
                date = convertDateToTimestamp("2024-08-15"),
                imageUrl = "https://example.com/degustazione_vini.jpg"
            ),
            Activity(
                name = "Escursione in montagna",
                description = "Goditi una giornata tra i sentieri di montagna.",
                price = 30.0,
                date = convertDateToTimestamp("2024-09-01"),
                imageUrl = "https://example.com/escursione_montagna.jpg"
            ),
            Activity(
                name = "Lezione di cucina",
                description = "Impara a cucinare piatti tipici con uno chef professionista.",
                price = 60.0,
                date = convertDateToTimestamp("2024-09-10"),
                imageUrl = "https://example.com/lezione_cucina.jpg"
            )
        )

        for (activity in predefinedActivities) {
            db.activityDao().insertActivity(activity)
        }
    }

    private fun convertDateToTimestamp(dateString: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateString)?.time ?: 0L
    }
}
