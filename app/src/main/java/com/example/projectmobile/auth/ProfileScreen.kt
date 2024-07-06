package com.example.projectmobile.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetails()
        Spacer(modifier = Modifier.height(16.dp))
        EditableUserInfo()
    }
}

@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Profilo",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.weight(1f)
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
fun ProfileDetails() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Immagine del profilo (da sostituire con l'immagine reale dell'utente)
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray)
        ) {
            // Immagine del profilo
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Nome utente (da sostituire con il nome reale dell'utente)
        Text(
            text = "Nome Utente",
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

@Composable
fun EditableUserInfo() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        item {
            EditableUserInfoItem(label = "Nome", value = "John")
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Cognome", value = "Doe")
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Email", value = "john.doe@example.com")
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Numero di telefono", value = "+1234567890")
            Spacer(modifier = Modifier.height(8.dp))
            DarkModeSwitch()
        }
    }
}

@Composable
fun EditableUserInfoItem(label: String, value: String) {
    Column {
        Text(text = label, style = TextStyle(fontSize = 18.sp))
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = { /* Implementa la modifica del valore */ },
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun DarkModeSwitch() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Tema scuro", style = TextStyle(fontSize = 18.sp))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = false,
            onCheckedChange = { /* Implementa il cambio tema */ }
        )
    }
}
