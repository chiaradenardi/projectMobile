package com.example.projectmobile.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmobile.auth.AuthManager
import com.example.projectmobile.data.*
import com.example.projectmobile.ui.components.PieSlice
import com.example.projectmobile.ui.components.toHex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityViewModel(
    private val activityDao: ActivityDao,
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao,
    context: Context
) : ViewModel() {
    private val db = AppDatabase.getInstance(context)
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    private val _favorites = MutableStateFlow<List<Activity>>(emptyList())
    val favorites: StateFlow<List<Activity>> = _favorites

    init {
        viewModelScope.launch {
            addPredefinedActivities()
            loadAllActivities()
            val currentUser = AuthManager.currentUser
            val username = currentUser?.username ?: ""
            _favorites.value = db.favoriteDao().getUserFavoriteActivities(username)
        }
    }

    fun filterActivitiesByCategory(category: String) {
        viewModelScope.launch {
            _activities.value = withContext(Dispatchers.IO) {
                if (category.isEmpty()) {
                    activityDao.getAllActivities()
                } else {
                    activityDao.getActivitiesByCategory(category)
                }
            }
        }
    }

    suspend fun getActivityById(activityId: Long): Activity? {
        val activity = db.activityDao().getActivityById(activityId)
        Log.d("ActivityViewModel", "Activity loaded: $activity")
        return activity
    }


    fun loadAllActivities() {
        viewModelScope.launch {
            _activities.value = withContext(Dispatchers.IO) {
                db.activityDao().getAllActivities()
            }
        }
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
                val userExists = userDao.getUserByUsername(username) != null
                val activityExists = activityDao.getActivityById(activity.id) != null

                if (userExists && activityExists) {
                    val favorite = Favorite(username = username, activityId = activity.id)
                    favoriteDao.insertFavorite(favorite)
                    _favorites.value = db.favoriteDao().getUserFavoriteActivities(username)
                    callback(true)
                } else {
                    callback(false)
                }
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    fun removeFromFavorites(activity: Activity, username: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                favoriteDao.deleteFavorite(username, activity.id)
                _favorites.value = db.favoriteDao().getUserFavoriteActivities(username)
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    suspend fun isFavorite(activityId: Long, username: String): Boolean {
        val favoriteActivities = favoriteDao.getUserFavoriteActivities(username)
        return favoriteActivities.any { it.id == activityId }
    }

    private suspend fun addPredefinedActivities() {
        val predefinedActivities = listOf(
            Activity(
                name = "Tour della città",
                description = "Esplora i luoghi storici della città.",
                price = 50.0,
                date = 1690848000L,
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Tourswilson.jpg/260px-Tourswilson.jpg",
                latitude = 45.0,
                longitude = 9.0,
                category = "Cultura",
                feedback = listOf(PieSlice("Positive", 50f, Color.Green.toHex())),
                phoneNumber = "1234567890"
            ),
            Activity(
                name = "Degustazione di vini",
                description = "Prova una selezione dei migliori vini locali.",
                price = 70.0,
                date = 1692144000L,
                imageUrl = "https://www.iltemporitrovato.org/wordpress/wp-content/uploads/Corso-di-Degustazione-Vini-7.jpg",
                latitude = 44.5,
                longitude = 9.5,
                category = "Gastronomia",
                feedback = listOf(
                    PieSlice("Sì", 80f, Color.Green.toHex()),
                    PieSlice("No", 5f, Color.Red.toHex()),
                    PieSlice("Neutro", 15f, Color.Gray.toHex())
                ),
                phoneNumber = "234-567-8901"
            ),
            Activity(
                name = "Escursione in montagna",
                description = "Goditi una giornata tra i sentieri di montagna.",
                price = 30.0,
                date = 1693526400L,
                imageUrl = "https://www.iltemporitrovato.org/wordpress/wp-content/uploads/Corso-di-Degustazione-Vini-7.jpg",
                latitude = 46.0,
                longitude = 8.0,
                category = "Natura",
                feedback = listOf(
                    PieSlice("Sì", 60f, Color.Green.toHex()),
                    PieSlice("No", 10f, Color.Red.toHex()),
                    PieSlice("Neutro", 30f, Color.Gray.toHex())
                ),
                phoneNumber = "345-678-9012"
            ),
            Activity(
                name = "Lezione di cucina",
                description = "Impara a cucinare piatti tipici con uno chef professionista.",
                price = 60.0,
                date = 1694304000L,
                imageUrl = "https://via.placeholder.com/150",
                latitude = 45.2,
                longitude = 8.5,
                category = "Gastronomia",
                feedback = listOf(
                    PieSlice("Sì", 75f, Color.Green.toHex()),
                    PieSlice("No", 10f, Color.Red.toHex()),
                    PieSlice("Neutro", 15f, Color.Gray.toHex())
                ),
                phoneNumber = "456-789-0123"
            )
        )

        val existingActivities = db.activityDao().getAllActivities()
        val existingActivityNames = existingActivities.map { it.name }

        for (activity in predefinedActivities) {
            if (activity.name !in existingActivityNames) {
                Log.d("ActivityViewModel", "Inserting activity: ${activity.name}")
                db.activityDao().insertActivity(activity)
            } else {
                Log.d("ActivityViewModel", "Activity already exists: ${activity.name}")
            }
        }
    }
}
