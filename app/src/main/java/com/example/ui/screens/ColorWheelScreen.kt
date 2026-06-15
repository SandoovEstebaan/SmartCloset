package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.GarmentEntity
import com.example.ui.components.ApparelGraphic
import com.example.ui.viewmodel.ClosetViewModel
import com.example.ui.viewmodel.ColorSegment
import com.example.ui.viewmodel.HarmonyResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorWheelScreen(
    viewModel: ClosetViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedIndex by viewModel.selectedColorIndex.collectAsState()
    val harmonies by viewModel.harmonies.collectAsState()
    val garments by viewModel.userGarments.collectAsState()

    // Query matched garments from user closet
    val matchingGarments = viewModel.retrieveSuggestedGarmentsByHarmonies(harmonies, garments)

    // Handle smooth rotation wheel state
    val wheelRotationTarget = -((selectedIndex * 30).toFloat()) // 12 segments = 360 / 12 = 30 deg
    val animatedRotation by animateFloatAsState(targetValue = wheelRotationTarget)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Círculo Cromático 🎨", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("color_wheel_back")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF6B4E28))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFDFBF7))
            )
        },
        containerColor = Color(0xFFFDFBF7)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Teoría del Color Inteligente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C4F43),
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp)
            )
            
            Text(
                text = "Selecciona un color para calcular combinaciones complementarias, análogas o triádicas y descubrir prendas en tu closet.",
                fontSize = 12.sp,
                color = Color(0xFF8C755E),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )

            // Dynamic rotation color wheel drawing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer circle container representing the rotating segments
                Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .rotate(animatedRotation)
                        .testTag("color_wheel_canvas")
                ) {
                    val width = size.width
                    val height = size.height
                    val thickness = 44.dp.toPx()

                    viewModel.colorWheelSegments.forEachIndexed { i, seg ->
                        val startAngle = i * 30f - 15f // centered arcs
                        drawArc(
                            color = seg.color,
                            startAngle = startAngle,
                            sweepAngle = 30f,
                            useCenter = false,
                            style = Stroke(width = thickness)
                        )
                    }

                    // Separation lines
                    viewModel.colorWheelSegments.forEachIndexed { i, _ ->
                        val angleRad = Math.toRadians((i * 30 - 15).toDouble())
                        val cos = Math.cos(angleRad).toFloat()
                        val sin = Math.sin(angleRad).toFloat()
                        val startR = width / 2 - thickness / 2
                        val endR = width / 2 + thickness / 2
                        
                        drawLine(
                            color = Color.White,
                            start = Offset(width / 2 + startR * cos, height / 2 + startR * sin),
                            end = Offset(width / 2 + endR * cos, height / 2 + endR * sin),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Elegant outline circle
                    drawCircle(
                        color = Color(0xFFE8DCCA),
                        radius = width / 2,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Centered pointer selector indicating currently chosen segment
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xFF8C755E), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(viewModel.colorWheelSegments[selectedIndex].color)
                                .border(1.dp, Color.LightGray, CircleShape)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = viewModel.colorWheelSegments[selectedIndex].name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A3E31)
                        )
                    }
                }
            }

            // Horizontal color chips slider for accessibility & clicking segments easily!
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("scrolling_wheel_selector")
            ) {
                itemsIndexed(viewModel.colorWheelSegments) { i, seg ->
                    val isSelected = i == selectedIndex
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.selectColorIndex(i) }
                            .testTag("segment_chip_$i")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(seg.color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFA56600) else Color(0xFFE8DCCA),
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = seg.name.take(4),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFA56600) else Color(0xFF5C4F43)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // HARMONIES DETAIL CARDS BLOCK
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Armonías de Color Estimadas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A3E31)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 1. Complementary (Opposite)
                HarmonyModuleCard(
                    title = "Complementario (Contraste Máximo)",
                    description = "Color diametralmente opuesto. Aporta dinamismo, contraste y vigor al vestuario.",
                    colors = listOf(harmonies.complementary),
                    modifier = Modifier.testTag("harmony_card_comp")
                )

                // 2. Analogous (Immediate adjacents)
                HarmonyModuleCard(
                    title = "Colores Análogos (Cohesión Fluida)",
                    description = "Adyacentes en el círculo cromático. Logra combinaciones armoniosas y sumamente agradables sin saltos abruptos.",
                    colors = harmonies.analogous,
                    modifier = Modifier.testTag("harmony_card_analog")
                )

                // 3. Triadic (Trío equilátero)
                HarmonyModuleCard(
                    title = "Triádicos (Riqueza y Equilibrio)",
                    description = "Tres colores equidistantes. Ofrece un look colorido y jovial pero balanceado.",
                    colors = harmonies.triadic,
                    modifier = Modifier.testTag("harmony_card_triad")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color(0xFFF0EAE1))
            Spacer(modifier = Modifier.height(18.dp))

            // SUGGESTED GARMENTS LIST IN WARDROBE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Prendas que combinan en tu Armario (${matchingGarments.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A3E31),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (matchingGarments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFF0EAE1), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tienes prendas que coincidan con esta armonía de color. ¡Visita \"Mi Armario\" para agregar ropa de color ${harmonies.primary.name}!",
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = Color(0xFF8C755E)
                        )
                    }
                } else {
                    // Small grid of matching clothes
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .testTag("harmony_closet_suggestions_grid")
                    ) {
                        items(matchingGarments) { item ->
                            val parsedChipColor = try {
                                Color(android.graphics.Color.parseColor(item.color))
                            } catch (e: Exception) {
                                Color(0xFFA56600)
                            }
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.size(44.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ApparelGraphic(
                                            imageName = item.imagen,
                                            garmentColor = parsedChipColor,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.nombre,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4A3E31),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${item.categoria} • ${item.colorName}",
                                            fontSize = 10.sp,
                                            color = Color(0xFF8C755E),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun HarmonyModuleCard(
    title: String,
    description: String,
    colors: List<ColorSegment>,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF6B4E28)
                )

                // Render colored harmony dots side-by-side
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    colors.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(c.color)
                                .border(1.dp, Color(0xFFE8DCCA), CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${colors.joinToString(" y ") { it.name }}: $description",
                fontSize = 11.sp,
                color = Color(0xFF5C4F43),
                lineHeight = 15.sp
            )
        }
    }
}
