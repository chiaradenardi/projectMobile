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
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull

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
                            validateLogin(context, email, password)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Errore durante il login: ${e.message}", Toast.LENGTH_SHORT).show()
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
    }
}

private suspend fun validateLogin(context: android.content.Context, email: String, password: String) {
    try {
        val userDao = AppDatabase.getInstance(context).userDao()
        val user = withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email).firstOrNull()
        }

        withContext(Dispatchers.Main) {
            if (user != null) {
                // User found, now check password
                if (user.password == password) {
                    // Password matched, proceed to home activity
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    // Finish current activity
                    (context as ComponentActivity).finish()
                } else {
                    // Password incorrect
                    Toast.makeText(context, "Password errata.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // User not found
                Toast.makeText(context, "Utente non trovato.", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            // Exception occurred during database operation
            Toast.makeText(context, "Errore durante il login: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        e.printStackTrace()
    }
}
