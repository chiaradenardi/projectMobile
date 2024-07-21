package com.example.projectmobile.data

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderWithBell(title: String, onBellClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onBellClick) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifiche",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}
