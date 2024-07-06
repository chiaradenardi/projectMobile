package com.example.projectmobile.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import com.example.projectmobile.data.User
import com.example.projectmobile.ui.theme.ProjectMobileTheme
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
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
                        registerUser(context, email, password)
                    }
                } else {
                    Toast.makeText(context, "Inserire email e password.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Registrati")
        }
    }
}

private suspend fun registerUser(context: android.content.Context, email: String, password: String) {
    try {
        val userDao = AppDatabase.getInstance(context).userDao()

        // For simplicity, you can set default values for firstName, lastName, and username
        val user = User(
            username = "default_username",
            email = email,
            password = password,
            firstName = "John",   // Example default value
            lastName = "Doe"      // Example default value
        )

        userDao.insertUser(user)
        Toast.makeText(context, "Registrazione completata.", Toast.LENGTH_SHORT).show()
        (context as ComponentActivity).finish() // Finish current activity
    } catch (e: Exception) {
        Toast.makeText(context, "Errore durante la registrazione: ${e.message}", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

