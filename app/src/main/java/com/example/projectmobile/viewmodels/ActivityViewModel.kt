package com.example.projectmobile.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmobile.auth.AuthManager
import com.example.projectmobile.data.Activity
import com.example.projectmobile.data.ActivityDao
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.data.Cart
import com.example.projectmobile.data.Favorite
import com.example.projectmobile.data.FavoriteDao
import com.example.projectmobile.data.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityViewModel(private val activityDao: ActivityDao, private val userDao: UserDao, private val favoriteDao: FavoriteDao, context: Context) : ViewModel() {
    private val db = AppDatabase.getInstance(context)
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    private val _favorites = MutableStateFlow<List<Activity>>(emptyList())
    val favorites: StateFlow<List<Activity>> = _favorites

    init {
        viewModelScope.launch {
            addPredefinedActivities()
            _activities.value = db.activityDao().getAllActivities()
            // Passa il nome utente corrente al ViewModel
            val currentUser = AuthManager.currentUser
            val username = currentUser?.username ?: ""
            _favorites.value = db.favoriteDao().getUserFavoriteActivities(username)
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

    fun addToFavorites(activity: Activity, username: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("ActivityViewModel", "Adding to favorites: ${activity.id} for user: $username")
                val userExists = userDao.getUserByUsername(username) != null
                val activityExists = activityDao.getActivityById(activity.id) != null

                if (userExists && activityExists) {
                    val favorite = Favorite(username = username, activityId = activity.id)
                    favoriteDao.insertFavorite(favorite)

                    // Ricarica i preferiti dopo l'aggiunta
                    _favorites.value = db.favoriteDao().getUserFavoriteActivities(username)

                    callback(true)
                } else {
                    Log.e("ActivityViewModel", "User or Activity does not exist: userExists=$userExists, activityExists=$activityExists")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error adding to favorites", e)
                callback(false)
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
