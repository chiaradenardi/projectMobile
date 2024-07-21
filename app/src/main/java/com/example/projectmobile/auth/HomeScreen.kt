package com.example.projectmobile.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.data.HeaderWithBell

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "HOME", onBellClick = {
            // Implementa l'azione per la campanella
        })
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
