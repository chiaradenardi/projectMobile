package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import com.example.projectmobile.utilis.HeaderWithBell
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.data.Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                val navController = rememberNavController()
                BookingsScreen(navController)
            }
        }
    }
}

@Composable
fun BookingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context) // Ottieni l'istanza del database
    var bookings by remember { mutableStateOf<List<Activity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        try {
            bookings = withContext(Dispatchers.IO) {
                database.activityDao().getAllActivities() // Ottieni le attività dal database
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Prenotazioni", onBellClick = {
            navController.navigate("notifications")
        })
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text("Errore: $error", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            EventList(bookings) // Passa la lista di prenotazioni all'EventList
        }
    }
}
