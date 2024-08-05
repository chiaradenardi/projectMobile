package com.example.projectmobile.auth

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.projectmobile.utilis.HeaderWithBell
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
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
                    ProfileScreen(navController, themeViewModel, AuthManager)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    authManager: AuthManager
) {
    val context = LocalContext.current
    val userDao = AppDatabase.getInstance(context).userDao()

    var user by remember { mutableStateOf<User?>(null) }
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    val darkMode by themeViewModel.isDarkTheme.collectAsState()

    val currentUser = authManager.currentUser
    val loggedInUsername = currentUser?.username

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var showBadge by remember { mutableStateOf(false) }
    var badgeMessage by remember { mutableStateOf("") }

    // Funzione per mostrare il badge
    fun showProgressBadge() {
        badgeMessage = "Hai modificato il tuo profilo!"
        showBadge = true
    }

    LaunchedEffect(loggedInUsername) {
        if (loggedInUsername != null) {
            Log.d("ProfileScreen", "LaunchedEffect triggered with username: $loggedInUsername")
            user = withContext(Dispatchers.IO) {
                userDao.getUserByUsername(loggedInUsername)
            }
            Log.d("ProfileScreen", "Fetched user: $user")
            user?.let {
                userName = TextFieldValue(it.username)
                firstName = it.firstName ?: ""
                lastName = it.lastName ?: ""
                userEmail = it.email
                userPhone = it.bio ?: ""
                themeViewModel.updateDarkTheme(it.darkMode)
            }
        } else {
            Log.d("ProfileScreen", "No logged in user")
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        content = { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
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
                        showProgressBadge()
                        coroutineScope.launch {
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
                                scaffoldState.snackbarHostState.showSnackbar("Modifiche salvate con successo")
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Salva")
                }
                if (showBadge) {
                    // Mostra il badge (può essere un banner, un dialogo o un Snackbar)
                    ShowProgressBadgeDialog(
                        showDialog = showBadge,
                        progressMessage = badgeMessage,
                        onDismiss = { showBadge = false }
                    )
                }
            }
        }
    )
}

@Composable
fun ShowProgressBadgeDialog(
    showDialog: Boolean,
    progressMessage: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Congratulazioni!") },
            text = { Text(text = progressMessage) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
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
fun ProfileDetails(
    userName: TextFieldValue,
    onUserNameChange: (TextFieldValue) -> Unit
) {
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileImageUri = uri
            // Save URI to user profile
        }
    )

    // Dialog to choose image from gallery
    var showImageSelectionDialog by remember { mutableStateOf(false) }
    if (showImageSelectionDialog) {
        ShowImageSelectionDialog(
            context = context,
            galleryLauncher = galleryLauncher,
            onDismiss = { showImageSelectionDialog = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colors.onBackground.copy(alpha = 0.2f))
                .clickable {
                    showImageSelectionDialog = true
                }
        ) {
            // Display profile image if available
            profileImageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
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
fun ShowImageSelectionDialog(
    context: Context,
    galleryLauncher: ActivityResultLauncher<String>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose Image") },
        buttons = {
            Column {
                Button(onClick = {
                    galleryLauncher.launch("image/*")
                    onDismiss()
                }) {
                    Text("Choose from Gallery")
                }
            }
        }
    )
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
