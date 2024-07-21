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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.data.HeaderWithBell

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                CartScreen()
            }
        }
    }
}

@Composable
fun CartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Carrello", onBellClick = {
            // Implementa l'azione per la campanella qui
        })
        Spacer(modifier = Modifier.height(16.dp))
        EventList() // Sostituisci con la lista degli articoli del carrello
    }
}
