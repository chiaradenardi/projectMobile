package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectmobile.data.HeaderWithBell

class BookingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                BookingsScreen()
            }
        }
    }
}

@Composable
fun BookingsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Prenotazioni", onBellClick = {
            // Implementa l'azione per la campanella qui
        })
        Spacer(modifier = Modifier.height(16.dp))
        EventList() // Sostituisci con la lista delle prenotazioni
    }
}
