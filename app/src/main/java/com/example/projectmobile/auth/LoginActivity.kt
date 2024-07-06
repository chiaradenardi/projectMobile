package com.example.projectmobile.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectmobile.data.AppDatabase
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectMobileTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    coroutineScope.launch {
                        try {
                            validateLogin(context, email, password) { message ->
                                snackbarMessage = message
                                snackbarVisible = true
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            snackbarMessage = "Errore durante il login: ${e.message}"
                            snackbarVisible = true
                        }
                    }
                } else {
                    Toast.makeText(context, "Inserire email e password.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Login")
        }

        // Show Snackbar for errors
        LaunchedEffect(snackbarVisible) {
            if (snackbarVisible) {
                // Delay to allow the snackbar to fully show before resetting state
                delay(3000)
                snackbarVisible = false
            }
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

private suspend fun validateLogin(
    context: android.content.Context,
    email: String,
    password: String,
    showErrorSnackbar: (String) -> Unit
) {
    try {
        val userDao = AppDatabase.getInstance(context).userDao()

        // Retrieve user from the database
        val user = withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }

        // Check if user exists and validate password
        withContext(Dispatchers.Main) {
            if (user != null) {
                // User found, now validate password
                if (user.password == password) {
                    // Password matched, proceed to home activity
                    context.startActivity(Intent(context, MainAppActivity::class.java))
                    // Finish current activity
                    (context as ComponentActivity).finish()
                } else {
                    // Password incorrect
                    showErrorSnackbar("Password errata.")
                }
            } else {
                // User not found
                showErrorSnackbar("Utente non trovato.")
            }
        }
    } catch (e: Exception) {
        // Handle exceptions
        showErrorSnackbar("Errore durante il login: ${e.message}")
        e.printStackTrace()
    }
}
