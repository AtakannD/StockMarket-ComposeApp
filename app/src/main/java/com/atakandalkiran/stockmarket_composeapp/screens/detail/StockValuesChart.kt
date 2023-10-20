package com.atakandalkiran.stockmarket_composeapp.screens.detail

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atakandalkiran.stockmarket_composeapp.domain.model.IntradayInfo
import kotlin.math.roundToInt

@Composable
fun DrawStockValuesChart(
    intradayInfo: List<IntradayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val blankSpace = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val maxValue = remember(intradayInfo) {
        (intradayInfo.maxOfOrNull { it.close }?.plus(1)?.roundToInt() ?: 0)
    }
    val minValue = remember(intradayInfo) {
        (intradayInfo.minOfOrNull { it.close }?.minus(1)?.roundToInt() ?: 0)
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Canvas(modifier = modifier) {
        val height = size.height
        val width = size.width
        val spacePerHour = (width - blankSpace) / intradayInfo.size
        (0 until intradayInfo.size -1 step 2).forEach { i ->
            val info = intradayInfo[i]
            val hour = info.date.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    blankSpace + i * spacePerHour,
                    height - 5,
                    textPaint
                )
            }
        }
        val priceStep = (maxValue - minValue) / 5f
        (0..4).forEach { i->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    (minValue + priceStep * i).toString(),
                    30f,
                    height - blankSpace - (i * height / 5f),
                    textPaint)
            }
        }
        var lastX = 0f
        val strokePath = Path().apply {
            for(i in intradayInfo.indices) {
                val currentInfo = intradayInfo[i]
                val nextInfo = intradayInfo.getOrNull(i + 1) ?: intradayInfo.last()
                val leftRatio = (currentInfo.close - minValue) / (maxValue - minValue)
                val rightRatio = (nextInfo.close - minValue) / (maxValue - minValue)

                val x1 = blankSpace + i * spacePerHour
                val y1 = height - blankSpace - (leftRatio * height).toFloat()
                val x2 = blankSpace + (i + 1) * spacePerHour
                val y2 = height - blankSpace - (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(
                        x = x1,
                        y = y1
                    )
                }
                lastX = (x1 + x2) / 2f
                quadraticBezierTo(
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, height - blankSpace)
                lineTo(blankSpace, height - blankSpace)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = height - blankSpace
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}