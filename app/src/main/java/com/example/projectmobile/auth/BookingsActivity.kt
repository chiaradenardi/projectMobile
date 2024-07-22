package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import com.example.projectmobile.utilis.HeaderWithBell

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

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Prenotazioni", onBellClick = {
            navController.navigate("notifications")
        })
        Spacer(modifier = Modifier.height(16.dp))
        EventList() // Sostituisci con la lista delle prenotazioni
    }
}
