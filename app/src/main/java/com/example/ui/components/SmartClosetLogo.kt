package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmartClosetLogo(
    modifier: Modifier = Modifier,
    textColor: Color = Color(0xFF6B4E28)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val width = size.width
            val height = size.height

            // 1. Círculo multicolor detrás del logo (Colorful gradient background)
            val multicolorBrush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFEAA220), // Warm beige gold
                    Color(0xFFEF4444), // Red
                    Color(0xFFEC4899), // Pink
                    Color(0xFF8B5CF6), // Purple
                    Color(0xFF3B82F6), // Blue
                    Color(0xFF10B981), // Green
                    Color(0xFFEAA220)  // Gold back
                ),
                center = Offset(width / 2, height / 2)
            )

            drawCircle(
                brush = multicolorBrush,
                radius = width * 0.44f,
                center = Offset(width / 2, height / 2)
            )

            // Inner mask white circle to make the gradient look like a ring outline
            drawCircle(
                color = Color.White,
                radius = width * 0.38f,
                center = Offset(width / 2, height / 2)
            )

            // 2. Una percha (Hanger)
            val hangerColor = Color(0xFF6B4E28)
            val strokeHanger = Stroke(width = 3.5.dp.toPx())

            // Hanger Hook (Gancho)
            val hookPath = Path().apply {
                moveTo(width * 0.5f, height * 0.32f)
                quadraticTo(width * 0.44f, height * 0.22f, width * 0.5f, height * 0.17f)
                quadraticTo(width * 0.56f, height * 0.12f, width * 0.60f, height * 0.20f)
            }
            drawPath(hookPath, color = hangerColor, style = strokeHanger)

            // Hanger Triangle base
            val hangerTriangle = Path().apply {
                moveTo(width * 0.5f, height * 0.32f)
                lineTo(width * 0.15f, height * 0.68f)
                quadraticTo(width * 0.5f, height * 0.58f, width * 0.85f, height * 0.68f)
                close()
            }
            drawPath(hangerTriangle, color = Color.White.copy(alpha = 0.5f))
            drawPath(hangerTriangle, color = hangerColor, style = strokeHanger)

            // 3. Un teléfono móvil integrado dentro del teléfono
            val phoneWidth = width * 0.25f
            val phoneHeight = height * 0.45f
            val phoneLeft = width * 0.375f
            val phoneTop = height * 0.38f
            val phoneCornerRad = 6.dp.toPx()

            // Draw full white body first
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(phoneLeft, phoneTop),
                size = Size(phoneWidth, phoneHeight),
                cornerRadius = CornerRadius(phoneCornerRad, phoneCornerRad)
            )

            // Draw border
            drawRoundRect(
                color = hangerColor,
                topLeft = Offset(phoneLeft, phoneTop),
                size = Size(phoneWidth, phoneHeight),
                cornerRadius = CornerRadius(phoneCornerRad, phoneCornerRad),
                style = Stroke(3.dp.toPx())
            )

            // Speaker notch
            drawLine(
                color = hangerColor,
                start = Offset(phoneLeft + phoneWidth * 0.35f, phoneTop + phoneHeight * 0.08f),
                end = Offset(phoneLeft + phoneWidth * 0.65f, phoneTop + phoneHeight * 0.08f),
                strokeWidth = 2.dp.toPx()
            )

            // Home button circle
            drawCircle(
                color = hangerColor,
                radius = 3.dp.toPx(),
                center = Offset(phoneLeft + phoneWidth * 0.5f, phoneTop + phoneHeight * 0.90f)
            )

            // 4. Un vestido dentro del teléfono
            val dressPath = Path().apply {
                // Top halter
                moveTo(phoneLeft + phoneWidth * 0.35f, phoneTop + phoneHeight * 0.22f)
                quadraticTo(
                    phoneLeft + phoneWidth * 0.5f, phoneTop + phoneHeight * 0.28f,
                    phoneLeft + phoneWidth * 0.65f, phoneTop + phoneHeight * 0.22f
                )
                // Fit midsection
                lineTo(phoneLeft + phoneWidth * 0.60f, phoneTop + phoneHeight * 0.45f)
                // Flared skirt
                lineTo(phoneLeft + phoneWidth * 0.75f, phoneTop + phoneHeight * 0.78f)
                lineTo(phoneLeft + phoneWidth * 0.25f, phoneTop + phoneHeight * 0.78f)
                lineTo(phoneLeft + phoneWidth * 0.40f, phoneTop + phoneHeight * 0.45f)
                close()
            }
            // Fill with cute warm coral pink
            drawPath(dressPath, color = Color(0xFFA56600))
            drawPath(dressPath, color = hangerColor, style = Stroke(2.dp.toPx()))
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        // 5. Texto "SmartCloset"
        Text(
            text = "SmartCloset",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = textColor
        )
    }
}
