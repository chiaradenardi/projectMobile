package com.example.projectmobile.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.projectmobile.data.Activity
import com.example.projectmobile.viewmodels.ActivityViewModel

@Composable
fun ActivityDetailScreen(navController: NavHostController, activityId: Long, viewModel: ActivityViewModel) {
    val activity by produceState<Activity?>(initialValue = null, activityId) {
        value = viewModel.getActivityById(activityId)
    }
    val scaffoldState = rememberScaffoldState()
    val username = "currentUsername" // Sostituisci con l'username dell'utente attuale
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // Monitoraggio dello stato del messaggio Snackbar
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            snackbarMessage = null // Reset the message after showing
        }
    }

    activity?.let { activity ->
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(activity.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(activity.imageUrl)
                        .crossfade(true)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = activity.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )

                LaunchedEffect(painter) {
                    if (painter.state is AsyncImagePainter.State.Error) {
                        Log.e("ActivityDetailScreen", "Errore nel caricamento dell'immagine: ${painter.state}")
                    } else if (painter.state is AsyncImagePainter.State.Loading) {
                        Log.d("ActivityDetailScreen", "Immagine in caricamento")
                    }
                }

                Text(
                    text = activity.name,
                    style = TextStyle(fontSize = 24.sp, color = Color.Black),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = activity.description,
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Prezzo: €${activity.price}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Call addToCart with username
                        viewModel.addToCart(activity, username)
                        // Trigger snackbar message
                        snackbarMessage = "Attività aggiunta al carrello"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aggiungi al Carrello")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Call addToFavorites with username
                        viewModel.addToFavorites(activity, username)
                        // Trigger snackbar message
                        snackbarMessage = "Attività aggiunta ai preferiti"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text("Aggiungi ai Preferiti")
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
