package com.example.projectmobile.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(val route: String, val icon: ImageVector, val title: String)

object NavigationItems {
    val Home = NavigationItem("home", Icons.Filled.Home, "Home")
    val Reservations = NavigationItem("reservations", Icons.Filled.DateRange, "Reservations")
    val Favorites = NavigationItem("favorites", Icons.Filled.Favorite, "Favorites")
    val Cart = NavigationItem("cart", Icons.Filled.ShoppingCart, "Cart")
    val Profile = NavigationItem("profile", Icons.Filled.Person, "Profile")
}
