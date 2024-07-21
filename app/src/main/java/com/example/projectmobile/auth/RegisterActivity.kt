package com.example.projectmobile.auth


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.data.User
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                RegisterScreen()
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = username,
            onValueChange = { username = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.medium),
            decorationBox = { innerTextField ->
                if (username.isEmpty()) {
                    Text("Username...", style = TextStyle(color = Color.Gray, fontSize = 18.sp))
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = email,
            onValueChange = { email = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.medium),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text("Email...", style = TextStyle(color = Color.Gray, fontSize = 18.sp))
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .background(Color.White, MaterialTheme.shapes.medium),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) {
                    Text("Password...", style = TextStyle(color = Color.Gray, fontSize = 18.sp))
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    coroutineScope.launch {
                        val result = registerUser(context, username, email, password)
                        when (result) {
                            RegistrationResult.Success -> {
                                Toast.makeText(context, "Registrazione completata.", Toast.LENGTH_SHORT).show()
                                (context as ComponentActivity).finish() // Finish current activity
                            }
                            RegistrationResult.UsernameAlreadyExists -> {
                                snackbarMessage = "Username già utilizzato."
                                snackbarVisible = true
                            }
                            RegistrationResult.EmailAlreadyExists -> {
                                snackbarMessage = "Email già utilizzata."
                                snackbarVisible = true
                            }
                            RegistrationResult.Error -> {
                                snackbarMessage = "Errore durante la registrazione."
                                snackbarVisible = true
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Inserire username, email e password.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Registrati")
        }

        if (snackbarVisible) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    Button(onClick = { snackbarVisible = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }
}

private enum class RegistrationResult {
    Success,
    UsernameAlreadyExists,
    EmailAlreadyExists,
    Error
}

private suspend fun registerUser(context: android.content.Context, username: String, email: String, password: String): RegistrationResult {
    return try {
        val userDao = AppDatabase.getInstance(context).userDao()

        // Check if username already exists
        val existingUserByUsername = userDao.getUserByUsername(username)
        if (existingUserByUsername != null) {
            return RegistrationResult.UsernameAlreadyExists
        }

        // Check if email already exists
        val existingUserByEmail = userDao.getUserByEmail(email)
        if (existingUserByEmail != null) {
            return RegistrationResult.EmailAlreadyExists
        }

        val user = User(
            username = username,
            email = email,
            password = password,
            firstName = "John",   // Example default value
            lastName = "Doe",      // Example default value
            darkMode = false
            )

        userDao.insertUser(user)
        RegistrationResult.Success
    } catch (e: Exception) {
        e.printStackTrace()
        RegistrationResult.Error
    }
}