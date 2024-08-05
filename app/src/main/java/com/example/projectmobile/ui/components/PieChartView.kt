package com.example.projectmobile.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.cos
import kotlin.math.sin


data class PieSlice(val label: String, val value: Float, val color: String)

fun Color.toHex(): String {
    return String.format("#%06X", 0xFFFFFF and toArgb())
}

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}
@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>
) {
    val total = slices.map { it.value }.sum()
    var startAngle = 0f

    Canvas(modifier = modifier.size(300.dp)) {
        slices.forEach { slice ->
            val sweepAngle = (slice.value / total) * 360f
            drawPieSlice(
                color = slice.color.toColor(), // Converti la stringa in un oggetto Color
                startAngle = startAngle,
                sweepAngle = sweepAngle
            )
            startAngle += sweepAngle
        }

        // Draw labels
        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                textSize = 40f
                color = android.graphics.Color.BLACK
                textAlign = android.graphics.Paint.Align.CENTER
            }
            startAngle = 0f
            slices.forEach { slice ->
                val sweepAngle = (slice.value / total) * 360f
                val labelAngle = startAngle + sweepAngle / 2
                val labelText = slice.label
                val x = (size.width / 2 + (size.width / 4) * cos(Math.toRadians(labelAngle.toDouble()))).toFloat()
                val y = (size.height / 2 + (size.height / 4) * sin(Math.toRadians(labelAngle.toDouble()))).toFloat()
                canvas.nativeCanvas.drawText(labelText, x, y, paint)
                startAngle += sweepAngle
            }
        }
    }
}


fun DrawScope.drawPieSlice(
    color: Color,
    startAngle: Float,
    sweepAngle: Float
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true
    )
}

