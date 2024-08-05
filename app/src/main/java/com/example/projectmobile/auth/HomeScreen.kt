package com.example.projectmobile.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.projectmobile.data.Activity
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.utilis.HeaderWithBell
import com.example.projectmobile.viewmodels.ActivityViewModel
import com.example.projectmobile.viewmodels.ActivityViewModelFactory

@Composable
fun HomeScreen(navController: NavHostController) {
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

    LaunchedEffect(Unit) {
        activityViewModel.loadAllActivities()
    }

    val activities by activityViewModel.activities.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "HOME", onBellClick = {
            navController.navigate("notifications")
        })
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Filtra in base ai tuoi interessi:",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        FilterRow(selectedCategory) { category ->
            selectedCategory = category
            activityViewModel.filterActivitiesByCategory(category)
        }
        Spacer(modifier = Modifier.height(16.dp))
        EventList(navController, activities)
    }
}



@Composable
fun EventList(navController: NavHostController, activities: List<Activity>) {
    LazyColumn {
        items(activities.take(4)) { activity ->
            EventListItem(navController, activity)
        }
    }
}

@Composable
fun EventListItem(navController: NavHostController, activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                navController.navigate("activity_detail/${activity.id}")
            },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colors.surface)
        ) {
            Text(
                text = activity.name,
                style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onSurface, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = activity.description,
                style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Prezzo: €${activity.price}",
                style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            )
        }
    }
}


@Composable
fun FilterRow(selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOf("Cultura", "Gastronomia", "Natura", "Sport", "Tutti")) { filter ->
            FilterButton(filter, selectedCategory) { selected ->
                onCategorySelected(selected)
            }
        }
    }
}

@Composable
fun FilterButton(text: String, selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    Button(
        onClick = {
            onCategorySelected(if (text == "Tutti") "" else text)
        },
        modifier = Modifier.padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (text == selectedCategory || (text == "Tutti" && selectedCategory.isNullOrEmpty())) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.surface
            },
            contentColor = if (text == selectedCategory || (text == "Tutti" && selectedCategory.isNullOrEmpty())) {
                MaterialTheme.colors.onPrimary
            } else {
                MaterialTheme.colors.onSurface
            }
        )
    ) {
        Text(text)
    }
}



