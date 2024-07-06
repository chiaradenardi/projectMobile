package com.example.projectmobile.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.ui.theme.ProjectMobileTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f) // Questo farà sì che occupi tutto lo spazio rimanente
        ) {
            TopSearchBar()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "HOME",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Filtra in base ai tuoi interessi:",
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilterRow()
            Spacer(modifier = Modifier.height(16.dp))
            EventList()
        }
        BottomNavigationBar()
    }
}

@Composable
fun TopSearchBar() {
    val searchText = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .weight(1f)
                .background(Color.White, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            decorationBox = { innerTextField ->
                if (searchText.value.isEmpty()) {
                    Text("Cerca eventi...", style = TextStyle(color = Color.Gray, fontSize = 18.sp))
                }
                innerTextField()
            }
        )
        IconButton(onClick = {
            // Implementa l'apertura della pagina delle notifiche
            // Esempio: startActivity(Intent(this, NotificationsActivity::class.java))
        }) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifiche",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun FilterRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(items = listOf("Cultura", "Gastronomia", "Natura", "Sport")) { filter ->
                FilterButton(filter)
            }
        }
    )
}

@Composable
fun FilterButton(text: String) {
    Button(
        onClick = {
            // Implementa il filtro degli eventi
        },
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text)
    }
}

@Composable
fun EventList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(10) { index ->
            EventListItem(index)
        }
    }
}

@Composable
fun EventListItem(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nome Evento $index",
                style = TextStyle(fontSize = 18.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Descrizione breve dell'evento $index",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        backgroundColor = Color.White,
        elevation = 8.dp,
        cutoutShape = MaterialTheme.shapes.small
    ) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Scopri") },
                selected = false,
                onClick = {
                    // Naviga a Home
                }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Prenotazioni") },
                label = { Text("Prenotazioni") },
                selected = false,
                onClick = {
                    // Naviga a Prenotazioni
                }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Preferiti") },
                label = { Text("Preferiti") },
                selected = false,
                onClick = {
                    // Naviga a Preferiti
                }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrello") },
                label = { Text("Carrello") },
                selected = false,
                onClick = {
                    // Naviga a Carrello
                }
            )
            BottomNavigationItem(
                icon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Profilo") },
                label = { Text("Profilo") },
                selected = false,
                onClick = {
                    // Naviga a Profilo
                }
            )
        }
    }
}

