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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.projectmobile.utilis.HeaderWithBell
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import com.example.projectmobile.ui.theme.ThemeViewModel

class ProfileActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            ProjectMobileTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    ProfileScreen(navController, themeViewModel)
                }
            }
        }
    }
}


@Composable
fun ProfileScreen(navController: NavHostController, themeViewModel: ThemeViewModel) {
    val context = LocalContext.current
    val userDao = AppDatabase.getInstance(context).userDao()

    var user by remember { mutableStateOf<User?>(null) }
    var userName by remember { mutableStateOf(TextFieldValue("NomeUtente")) }
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Doe") }
    var userEmail by remember { mutableStateOf("email@example.com") }
    var userPhone by remember { mutableStateOf("+1234567890") }
    val darkMode by themeViewModel.isDarkTheme.collectAsState()

    LaunchedEffect(Unit) {
        user = withContext(Dispatchers.IO) {
            userDao.getUserByUsername("NomeUtente")
        }
        user?.let {
            userName = TextFieldValue(it.username)
            firstName = it.firstName
            lastName = it.lastName
            userEmail = it.email
            userPhone = it.bio ?: ""
            themeViewModel.updateDarkTheme(it.darkMode)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderWithBell(title = "Profilo", onBellClick = {
            navController.navigate("notifications")
        })
        Spacer(modifier = Modifier.height(16.dp))
        ProfileDetails(userName = userName, onUserNameChange = { userName = it })
        Spacer(modifier = Modifier.height(16.dp))
        EditableUserInfo(
            firstName = firstName,
            lastName = lastName,
            userEmail = userEmail,
            userPhone = userPhone,
            darkMode = darkMode,
            onFirstNameChange = { firstName = it },
            onLastNameChange = { lastName = it },
            onUserEmailChange = { userEmail = it },
            onUserPhoneChange = { userPhone = it },
            onDarkModeChange = {
                themeViewModel.updateDarkTheme(it)
            }
        )
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    user?.let {
                        userDao.updateUser(
                            it.copy(
                                username = userName.text,
                                email = userEmail,
                                firstName = firstName,
                                lastName = lastName,
                                bio = userPhone,
                                darkMode = darkMode
                            )
                        )
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Salva")
        }
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Immagine del profilo
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.2f))
        ) {
            // Sostituire con l'immagine reale dell'utente
        }
        Spacer(modifier = Modifier.width(16.dp))
        // Nome utente modificabile
        BasicTextField(
            value = userName,
            onValueChange = onUserNameChange,
            textStyle = TextStyle(fontSize = 20.sp, color = MaterialTheme.colors.onBackground),
            modifier = Modifier
                .border(1.dp, MaterialTheme.colors.onSurface, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
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
    darkMode: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onUserEmailChange: (String) -> Unit,
    onUserPhoneChange: (String) -> Unit,
    onDarkModeChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
            DarkModeSwitch(isChecked = darkMode, onCheckedChange = onDarkModeChange)
        }
    }
}

@Composable
fun EditableUserInfoItem(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
        )
    }
}

@Composable
fun DarkModeSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "Tema scuro", style = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground))
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.secondary,
                uncheckedThumbColor = MaterialTheme.colors.onSurface
            )
        )
    }
}
