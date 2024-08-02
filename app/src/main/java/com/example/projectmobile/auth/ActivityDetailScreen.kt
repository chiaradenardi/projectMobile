package com.example.projectmobile.auth

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.projectmobile.ui.components.PieChart
import com.example.projectmobile.ui.components.PieSlice
import com.example.projectmobile.viewmodels.ActivityViewModel
import java.util.Date

@Composable
fun ActivityDetailScreen(navController: NavHostController, activityId: Long, viewModel: ActivityViewModel) {
    val context = LocalContext.current
    val activity by produceState<Activity?>(initialValue = null, activityId) {
        value = viewModel.getActivityById(activityId)
    }
    val scaffoldState = rememberScaffoldState()
    val currentUser = AuthManager.currentUser
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(activityId) {
        currentUser?.username?.let { username ->
            isFavorite = viewModel.isFavorite(activityId, username)
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            snackbarMessage = null
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
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                val imagePainter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(activity.imageUrl)
                        .crossfade(true)
                        .build()
                )

                when (imagePainter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Log.d("ActivityDetailScreen", "Immagine in caricamento per URL: ${activity.imageUrl}")
                    }
                    is AsyncImagePainter.State.Error -> {
                        val errorState = imagePainter.state as AsyncImagePainter.State.Error
                        Log.e("ActivityDetailScreen", "Errore nel caricamento dell'immagine: ${errorState.result.throwable}")
                    }
                    else -> {
                        Log.i("ActivityDetailScreen", "Stato dell'immagine: ${imagePainter.state}")
                    }
                }

                Image(
                    painter = imagePainter,
                    contentDescription = activity.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

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
                Text(
                    text = "Data: ${Date(activity.date).toLocaleString()}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val pieData = activity.feedback ?: emptyList()
                Log.d("ActivityDetailScreen", "Feedback data: $pieData")

                Text(
                    text = "Feedback gradimento attività:",
                    style = TextStyle(fontSize = 18.sp, color = Color.Black),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                PieChart(slices = pieData, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Chiamaci per più informazioni!",
                    style = TextStyle(fontSize = 18.sp, color = Color.Black),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Log.d("ActivityDetailScreen", "telefono: ${activity.phoneNumber}")

                Text(
                    text = "Telefono: ${activity.phoneNumber}",
                    style = TextStyle(fontSize = 16.sp, color = Color.Blue),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${activity.phoneNumber}"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Log.e("ActivityDetailScreen", "Errore nell'aprire la chiamata: ${e.message}")
                                snackbarMessage = "Errore nell'aprire la chiamata"
                            }
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val mapUri = "https://www.openstreetmap.org/?mlat=${activity.latitude}&mlon=${activity.longitude}&zoom=12"
                Text(
                    text = "Visualizza sulla mappa",
                    style = TextStyle(fontSize = 16.sp, color = Color.Blue),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUri))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Log.e("ActivityDetailScreen", "Errore nell'aprire la mappa: ${e.message}")
                                snackbarMessage = "Errore nell'aprire la mappa"
                            }
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.addToCart(activity, currentUser?.username ?: "")
                        snackbarMessage = "Attività aggiunta al carrello"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aggiungi al Carrello")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val username = currentUser?.username ?: ""
                        if (isFavorite) {
                            viewModel.removeFromFavorites(activity, username) { success ->
                                if (success) {
                                    isFavorite = false
                                    snackbarMessage = "Attività rimossa dai preferiti"
                                } else {
                                    snackbarMessage = "Errore nella rimozione dai preferiti"
                                }
                            }
                        } else {
                            viewModel.addToFavorites(activity, username) { success ->
                                if (success) {
                                    isFavorite = true
                                    snackbarMessage = "Attività aggiunta ai preferiti"
                                } else {
                                    snackbarMessage = "Errore nell'aggiungere ai preferiti"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (isFavorite) Color.Red else Color.Gray)
                ) {
                    Text(if (isFavorite) "Rimuovi dai Preferiti" else "Aggiungi ai Preferiti")
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
