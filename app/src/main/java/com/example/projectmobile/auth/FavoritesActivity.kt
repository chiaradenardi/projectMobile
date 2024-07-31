package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectmobile.utilis.HeaderWithBell
import com.example.projectmobile.data.Activity
import com.example.projectmobile.viewmodels.ActivityViewModel

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                val navController = rememberNavController()
                val activityViewModel: ActivityViewModel = viewModel()
                FavoritesScreen(navController, activityViewModel)
            }
        }
    }
}

@Composable
fun FavoritesScreen(navController: NavHostController, viewModel: ActivityViewModel) {
    val favoriteActivities by viewModel.favorites.collectAsState()
    val isLoading = favoriteActivities.isEmpty()

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Preferiti", onBellClick = {
            navController.navigate("notifications")
        })
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            EventList(navController, favoriteActivities)
        }
    }
}
