package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ApparelGraphic(
    imageName: String,
    garmentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFFFCFAF7), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE8DCCA), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().aspectRatio(1f)) {
            val width = size.width
            val height = size.height
            val strokeWidth = 3.dp.toPx()

            when (imageName) {
                "t_shirt_beige", "t_shirt_white", "t_shirt_brown", "t_shirt_orange" -> {
                    // Draw a T-Shirt
                    val path = Path().apply {
                        moveTo(width * 0.3f, height * 0.15f)
                        // Left sleeve
                        lineTo(width * 0.15f, height * 0.3f)
                        lineTo(width * 0.25f, height * 0.4f)
                        lineTo(width * 0.35f, height * 0.32f)
                        // Torso left
                        lineTo(width * 0.35f, height * 0.85f)
                        // Hem bottom
                        lineTo(width * 0.65f, height * 0.85f)
                        // Torso right
                        lineTo(width * 0.65f, height * 0.32f)
                        // Right sleeve
                        lineTo(width * 0.75f, height * 0.4f)
                        lineTo(width * 0.85f, height * 0.3f)
                        lineTo(width * 0.7f, height * 0.15f)
                        // Neckline curve
                        quadraticTo(width * 0.5f, height * 0.25f, width * 0.3f, height * 0.15f)
                        close()
                    }
                    drawPath(path, color = garmentColor)
                    drawPath(path, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }

                "shirt_blue" -> {
                    // Dress Shirt with collar lines
                    val path = Path().apply {
                        moveTo(width * 0.3f, height * 0.12f)
                        lineTo(width * 0.12f, height * 0.25f)
                        lineTo(width * 0.25f, height * 0.88f)
                        lineTo(width * 0.75f, height * 0.88f)
                        lineTo(width * 0.88f, height * 0.25f)
                        lineTo(width * 0.7f, height * 0.12f)
                        quadraticTo(width * 0.5f, height * 0.2f, width * 0.3f, height * 0.12f)
                        close()
                    }
                    drawPath(path, color = garmentColor)
                    drawPath(path, color = Color(0xFF6B5843), style = Stroke(strokeWidth))

                    // Collar lines
                    drawPath(Path().apply {
                        moveTo(width * 0.3f, height * 0.12f)
                        lineTo(width * 0.5f, height * 0.28f)
                        lineTo(width * 0.7f, height * 0.12f)
                    }, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    
                    // Button lines down the center
                    drawLine(
                        color = Color(0xFF6B5843),
                        start = Offset(width * 0.5f, height * 0.28f),
                        end = Offset(width * 0.5f, height * 0.88f),
                        strokeWidth = strokeWidth
                    )
                }

                "dress_elegant" -> {
                    // Dress path
                    val path = Path().apply {
                        moveTo(width * 0.35f, height * 0.10f)
                        quadraticTo(width * 0.5f, height * 0.15f, width * 0.65f, height * 0.10f)
                        lineTo(width * 0.7f, height * 0.35f)
                        quadraticTo(width * 0.5f, height * 0.45f, width * 0.3f, height * 0.35f)
                        close()
                    }
                    val lowerPath = Path().apply {
                        moveTo(width * 0.32f, height * 0.35f)
                        lineTo(width * 0.15f, height * 0.90f)
                        lineTo(width * 0.85f, height * 0.90f)
                        lineTo(width * 0.68f, height * 0.35f)
                        quadraticTo(width * 0.5f, height * 0.42f, width * 0.32f, height * 0.35f)
                        close()
                    }
                    drawPath(path, color = garmentColor)
                    drawPath(lowerPath, color = garmentColor)
                    drawPath(path, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    drawPath(lowerPath, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }

                "pants_tan", "pants_blue", "pants_black" -> {
                    // Pants/Trousers with belt loops
                    val path = Path().apply {
                        moveTo(width * 0.28f, height * 0.15f)
                        lineTo(width * 0.72f, height * 0.15f)
                        lineTo(width * 0.78f, height * 0.88f)
                        lineTo(width * 0.54f, height * 0.88f)
                        lineTo(width * 0.5f, height * 0.45f) // crotch
                        lineTo(width * 0.46f, height * 0.88f)
                        lineTo(width * 0.22f, height * 0.88f)
                        close()
                    }
                    drawPath(path, color = garmentColor)
                    drawPath(path, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }

                "jacket_leather", "jacket_sport" -> {
                    // Jacket path (outer shell and zipper)
                    val base = Path().apply {
                        moveTo(width * 0.28f, height * 0.15f)
                        lineTo(width * 0.15f, height * 0.35f)
                        lineTo(width * 0.25f, height * 0.85f)
                        lineTo(width * 0.75f, height * 0.85f)
                        lineTo(width * 0.85f, height * 0.35f)
                        lineTo(width * 0.72f, height * 0.15f)
                        close()
                    }
                    drawPath(base, color = garmentColor)
                    drawPath(base, color = Color(0xFF6B5843), style = Stroke(strokeWidth))

                    // Open zipper detail
                    drawPath(Path().apply {
                        moveTo(width * 0.35f, height * 0.15f)
                        lineTo(width * 0.5f, height * 0.45f)
                        lineTo(width * 0.65f, height * 0.15f)
                    }, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    
                    drawLine(
                        color = Color(0xFF6B5843),
                        start = Offset(width * 0.5f, height * 0.45f),
                        end = Offset(width * 0.5f, height * 0.85f),
                        strokeWidth = strokeWidth
                    )
                }

                "shoes_loafer" -> {
                    // Smart dress loafers
                    val leftLoafer = Path().apply {
                        moveTo(width * 0.15f, height * 0.45f)
                        quadraticTo(width * 0.3f, height * 0.3f, width * 0.45f, height * 0.45f)
                        lineTo(width * 0.42f, height * 0.75f)
                        quadraticTo(width * 0.3f, height * 0.82f, width * 0.18f, height * 0.75f)
                        close()
                    }
                    val rightLoafer = Path().apply {
                        moveTo(width * 0.55f, height * 0.45f)
                        quadraticTo(width * 0.7f, height * 0.3f, width * 0.85f, height * 0.45f)
                        lineTo(width * 0.82f, height * 0.75f)
                        quadraticTo(width * 0.7f, height * 0.82f, width * 0.58f, height * 0.75f)
                        close()
                    }
                    drawPath(leftLoafer, color = garmentColor)
                    drawPath(rightLoafer, color = garmentColor)
                    drawPath(leftLoafer, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    drawPath(rightLoafer, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }

                "shoes_sneaker" -> {
                    // Hip sporty sneakers
                    drawRoundRect(
                        color = garmentColor,
                        topLeft = Offset(width * 0.15f, height * 0.4f),
                        size = Size(width * 0.32f, height * 0.35f),
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                    drawRoundRect(
                        color = garmentColor,
                        topLeft = Offset(width * 0.53f, height * 0.4f),
                        size = Size(width * 0.32f, height * 0.35f),
                        cornerRadius = CornerRadius(10.dp.toPx())
                    )
                    // White sole stripes
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(width * 0.15f, height * 0.7f),
                        size = Size(width * 0.32f, height * 0.08f)
                    )
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(width * 0.53f, height * 0.7f),
                        size = Size(width * 0.32f, height * 0.08f)
                    )
                    // Outlines
                    drawRoundRect(
                        color = Color(0xFF6B5843),
                        topLeft = Offset(width * 0.15f, height * 0.4f),
                        size = Size(width * 0.32f, height * 0.38f),
                        cornerRadius = CornerRadius(10.dp.toPx()),
                        style = Stroke(strokeWidth)
                    )
                    drawRoundRect(
                        color = Color(0xFF6B5843),
                        topLeft = Offset(width * 0.53f, height * 0.4f),
                        size = Size(width * 0.32f, height * 0.38f),
                        cornerRadius = CornerRadius(10.dp.toPx()),
                        style = Stroke(strokeWidth)
                    )
                }

                "bag_leather" -> {
                    // Handbag shape
                    val body = Path().apply {
                        moveTo(width * 0.25f, height * 0.4f)
                        lineTo(width * 0.75f, height * 0.4f)
                        quadraticTo(width * 0.8f, height * 0.85f, width * 0.7f, height * 0.85f)
                        lineTo(width * 0.30f, height * 0.85f)
                        quadraticTo(width * 0.2f, height * 0.85f, width * 0.25f, height * 0.4f)
                        close()
                    }
                    val strap = Path().apply {
                        moveTo(width * 0.35f, height * 0.4f)
                        quadraticTo(width * 0.5f, height * 0.15f, width * 0.65f, height * 0.4f)
                    }
                    drawPath(body, color = garmentColor)
                    drawPath(body, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    drawPath(strap, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }

                "acc_glasses" -> {
                    // Sunglasses
                    val leftGlass = Path().apply {
                        addOval(
                            androidx.compose.ui.geometry.Rect(
                                left = width * 0.18f,
                                top = height * 0.35f,
                                right = width * 0.45f,
                                bottom = height * 0.65f
                            )
                        )
                    }
                    val rightGlass = Path().apply {
                        addOval(
                            androidx.compose.ui.geometry.Rect(
                                left = width * 0.55f,
                                top = height * 0.35f,
                                right = width * 0.82f,
                                bottom = height * 0.65f
                            )
                        )
                    }
                    drawPath(leftGlass, color = garmentColor)
                    drawPath(rightGlass, color = garmentColor)
                    drawPath(leftGlass, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    drawPath(rightGlass, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                    // Connect bridge
                    drawLine(
                        color = Color(0xFF6B5843),
                        start = Offset(width * 0.45f, height * 0.45f),
                        end = Offset(width * 0.55f, height * 0.45f),
                        strokeWidth = strokeWidth
                    )
                }

                "jewelry_watch" -> {
                    // Watch band and dial face
                    drawRoundRect(
                        color = Color(0xFF708090),
                        topLeft = Offset(width * 0.42f, height * 0.15f),
                        size = Size(width * 0.16f, height * 0.7f),
                        cornerRadius = CornerRadius(5.dp.toPx())
                    )
                    drawCircle(
                        color = garmentColor,
                        radius = width * 0.22f,
                        center = Offset(width * 0.5f, height * 0.5f)
                    )
                    drawCircle(
                        color = Color(0xFF6B5843),
                        radius = width * 0.22f,
                        center = Offset(width * 0.5f, height * 0.5f),
                        style = Stroke(strokeWidth)
                    )
                    // Watch hands
                    drawLine(
                        color = Color(0xFF6B5843),
                        start = Offset(width * 0.5f, height * 0.5f),
                        end = Offset(width * 0.5f, height * 0.38f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = Color(0xFF6B5843),
                        start = Offset(width * 0.5f, height * 0.5f),
                        end = Offset(width * 0.62f, height * 0.54f),
                        strokeWidth = strokeWidth
                    )
                }

                else -> {
                    // Fallback using generic apparel hanger / percha!
                    drawCircle(
                        color = garmentColor.copy(alpha = 0.3f),
                        radius = width * 0.4f,
                        center = Offset(width * 0.5f, height * 0.5f)
                    )
                    drawCircle(
                        color = Color(0xFF6B5843),
                        radius = width * 0.4f,
                        center = Offset(width * 0.5f, height * 0.5f),
                        style = Stroke(2.dp.toPx())
                    )
                    // Draw a cute hanger path!
                    val path = Path().apply {
                        moveTo(width * 0.5f, height * 0.28f)
                        quadraticTo(width * 0.45f, height * 0.18f, width * 0.5f, height * 0.15f)
                        quadraticTo(width * 0.55f, height * 0.12f, width * 0.58f, height * 0.18f)
                        moveTo(width * 0.5f, height * 0.28f)
                        lineTo(width * 0.2f, height * 0.55f)
                        quadraticTo(width * 0.5f, height * 0.48f, width * 0.8f, height * 0.55f)
                        close()
                        // Cross hanger bottom bar
                        moveTo(width * 0.2f, height * 0.55f)
                        lineTo(width * 0.8f, height * 0.55f)
                    }
                    drawPath(path, color = Color(0xFF6B5843), style = Stroke(strokeWidth))
                }
            }
        }
    }
}
