package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import com.example.projectmobile.ui.theme.ThemeViewModel
import com.example.projectmobile.viewmodels.ActivityViewModel
import com.example.projectmobile.viewmodels.ActivityViewModelFactory

class MainAppActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize AuthManager here if needed
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            ProjectMobileTheme(darkTheme = isDarkTheme) {
                MainAppScreen(themeViewModel, AuthManager) // Pass AuthManager here
            }
        }
    }

    @Composable
    fun MainAppScreen(themeViewModel: ThemeViewModel, authManager: AuthManager) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val context = LocalContext.current
        val database = AppDatabase.getInstance(context)
        val activityViewModel: ActivityViewModel = viewModel(
            factory = ActivityViewModelFactory(
                context,
                database.activityDao(),
                database.userDao(),
                database.favoriteDao()
            )
        )
        Scaffold(
            bottomBar = {
                if (currentRoute != "activity_detail/{activityId}" && currentRoute != "notifications") {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomeScreen(navController) }
                composable("profile") { ProfileScreen(navController, themeViewModel, authManager) } // Pass AuthManager here
                composable("favorites") { FavoritesScreen(navController, activityViewModel) }
                composable("notifications") { NotificationsScreen(navController) }
                composable(
                    "activity_detail/{activityId}",
                    arguments = listOf(navArgument("activityId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val activityId = backStackEntry.arguments?.getLong("activityId")
                    activityId?.let {
                        ActivityDetailScreen(navController, it, activityViewModel)
                    }
                }
            }
        }
    }
}


    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            NavigationItem("home", Icons.Default.Home, "Home"),
            NavigationItem("favorites", Icons.Default.Favorite, "Favorites"),
            NavigationItem("profile", Icons.Default.Person, "Profile")
        )

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 8.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }

