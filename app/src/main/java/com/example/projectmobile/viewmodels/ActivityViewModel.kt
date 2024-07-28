package com.example.projectmobile.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmobile.data.Activity
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.data.Cart
import com.example.projectmobile.data.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    suspend fun getActivityById(activityId: Long): Activity? {
        return db.activityDao().getActivityById(activityId)
    }

    fun addToCart(activity: Activity, username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cartItem = Cart(username = username, activityId = activity.id)
                db.cartDao().insertCartItem(cartItem)
            }
        }
    }

    fun addToFavorites(activity: Activity, username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favoriteItem = Favorite(username = username, activityId = activity.id)
                db.favoriteDao().insertFavorite(favoriteItem)
            }
        }
    }

    private suspend fun addPredefinedActivities() {
        val predefinedActivities = listOf(
            Activity(
                name = "Tour della città",
                description = "Esplora i luoghi storici della città.",
                price = 50.0,
                date = 1690848000L, // Esempio di data
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Tourswilson.jpg/260px-Tourswilson.jpg",
                latitude = 45.0,  // Esempio di latitudine
                longitude = 9.0   // Esempio di longitudine
            ),
            Activity(
                name = "Degustazione di vini",
                description = "Prova una selezione dei migliori vini locali.",
                price = 70.0,
                date = 1692144000L, // Esempio di data
                imageUrl = "https://www.iltemporitrovato.org/wordpress/wp-content/uploads/Corso-di-Degustazione-Vini-7.jpg",
                latitude = 44.5,  // Esempio di latitudine
                longitude = 9.5   // Esempio di longitudine
            ),
            Activity(
                name = "Escursione in montagna",
                description = "Goditi una giornata tra i sentieri di montagna.",
                price = 30.0,
                date = 1693526400L, // Esempio di data
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Montagna_di_Campli%2C_Teramo%2C_Italy%2C_south_slope.jpg/1200px-Montagna_di_Campli%2C_Teramo%2C_Italy%2C_south_slope.jpg?20220519194414",
                latitude = 46.0,  // Esempio di latitudine
                longitude = 8.0   // Esempio di longitudine
            ),
            Activity(
                name = "Lezione di cucina",
                description = "Impara a cucinare piatti tipici con uno chef professionista.",
                price = 60.0,
                date = 1694304000L, // Esempio di data
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/1200px-Good_Food_Display_-_NCI_Visuals_Online.jpg?20100603030321",
                latitude = 45.2,  // Esempio di latitudine
                longitude = 8.5   // Esempio di longitudine
            )
        )

        for (activity in predefinedActivities) {
            db.activityDao().insertActivity(activity)
        }
    }
}