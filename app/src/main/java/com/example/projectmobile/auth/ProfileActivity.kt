package com.example.projectmobile.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    // Stato per i dettagli del profilo
    var userName by remember { mutableStateOf(TextFieldValue("NomeUtente")) }
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Doe") }
    var userEmail by remember { mutableStateOf("email@example.com") }
    var userPhone by remember { mutableStateOf("+1234567890") }
    var darkMode by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader()
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetails(
            userName = userName,
            onUserNameChange = { userName = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditableUserInfo(
            firstName = firstName,
            lastName = lastName,
            userEmail = userEmail,
            userPhone = userPhone,
            onFirstNameChange = { firstName = it },
            onLastNameChange = { lastName = it },
            onUserEmailChange = { userEmail = it },
            onUserPhoneChange = { userPhone = it },
            onDarkModeChange = { darkMode = it }
        )
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
fun ProfileDetails(userName: TextFieldValue, onUserNameChange: (TextFieldValue) -> Unit) {
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
        // Nome utente modificabile
        BasicTextField(
            value = userName,
            onValueChange = onUserNameChange,
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .border(1.dp, Color.Gray, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(Color.White, MaterialTheme.shapes.medium)
                .fillMaxWidth()
        )
    }
}

@Composable
fun EditableUserInfo(
    firstName: String,
    lastName: String,
    userEmail: String,
    userPhone: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onUserEmailChange: (String) -> Unit,
    onUserPhoneChange: (String) -> Unit,
    onDarkModeChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        item {
            EditableUserInfoItem(label = "Nome", value = firstName, onValueChange = onFirstNameChange)
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Cognome", value = lastName, onValueChange = onLastNameChange)
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Email", value = userEmail, onValueChange = onUserEmailChange)
            Spacer(modifier = Modifier.height(8.dp))
            EditableUserInfoItem(label = "Numero di telefono", value = userPhone, onValueChange = onUserPhoneChange)
            Spacer(modifier = Modifier.height(8.dp))
            DarkModeSwitch(isChecked = false, onCheckedChange = onDarkModeChange)
        }
    }
}

@Composable
fun EditableUserInfoItem(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, style = TextStyle(fontSize = 18.sp))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun DarkModeSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Tema scuro", style = TextStyle(fontSize = 18.sp))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}
