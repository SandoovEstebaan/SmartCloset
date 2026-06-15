package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.GarmentEntity
import com.example.data.database.OutfitEntity
import com.example.ui.components.ApparelGraphic
import com.example.ui.viewmodel.ClosetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutfitsScreen(
    viewModel: ClosetViewModel,
    onNavigateBack: () -> Unit
) {
    val garments by viewModel.userGarments.collectAsState()
    val savedOutfits by viewModel.savedOutfits.collectAsState()

    val occasionsList = listOf("Casual", "Formal", "Deportivo", "Elegante", "Fiesta", "Oficina")
    var selectedOccasion by remember { mutableStateOf(occasionsList[0]) }

    // Smart Suggestion State Builders
    // Filter matching occasion or default fallback if empty
    var topItem by remember { mutableStateOf<GarmentEntity?>(null) }
    var bottomItem by remember { mutableStateOf<GarmentEntity?>(null) }
    var shoesItem by remember { mutableStateOf<GarmentEntity?>(null) }
    var accItem by remember { mutableStateOf<GarmentEntity?>(null) }
    var suggestionLog by remember { mutableStateOf("") }

    // Recompute smart proposal when category or wardrobe changes
    remember(selectedOccasion, garments) {
        val uppers = garments.filter { it.categoria == "Camisas" || it.categoria == "Camisetas" || it.categoria == "Vestidos" }
        val bottoms = garments.filter { it.categoria == "Pantalones" }
        val shoes = garments.filter { it.categoria == "Zapatos" }
        val accessories = garments.filter { it.categoria == "Accesorios" || it.categoria == "Bolsos" || it.categoria == "Chaquetas" || it.categoria == "Joyas" }

        if (garments.isNotEmpty()) {
            // Pick upper matching occasion, fallback to random
            val matchedUpper = uppers.firstOrNull { it.ocasion == selectedOccasion } ?: uppers.shuffled().firstOrNull()
            // Pick bottom matching occasion, fallback to random. Note: if selected upper is a Dress (Vestido), no bottom is strictly required!
            val matchedBottom = if (matchedUpper?.categoria == "Vestidos") {
                null
            } else {
                bottoms.firstOrNull { it.ocasion == selectedOccasion } ?: bottoms.shuffled().firstOrNull()
            }
            // Pick shoes matching occasion, fallback to random
            val matchedShoes = shoes.firstOrNull { it.ocasion == selectedOccasion } ?: shoes.shuffled().firstOrNull()
            // Pick acc matching occasion, fallback to random
            val matchedAcc = accessories.firstOrNull { it.ocasion == selectedOccasion } ?: accessories.shuffled().firstOrNull()

            topItem = matchedUpper
            bottomItem = matchedBottom
            shoesItem = matchedShoes
            accItem = matchedAcc

            // Generate smart commentary based on selection
            suggestionLog = if (matchedUpper != null) {
                val upC = matchedUpper.colorName
                val bC = matchedBottom?.colorName ?: "Completo"
                "Sugerencia $selectedOccasion óptima coordinando ${matchedUpper.nombre} ($upC) con " +
                (if (matchedBottom != null) "${matchedBottom.nombre} ($bC)" else "atuendo entero (Vestido)") + 
                "."
            } else {
                "Sugerencias generadas para combinación."
            }
        } else {
            topItem = null
            bottomItem = null
            shoesItem = null
            accItem = null
            suggestionLog = "Agrega algunas prendas a tu armario virtual para generar combinaciones automáticas."
        }
        true
    }

    var successSaveBanner by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Outfits Inteligentes ✨", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("outfits_btn_back")
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
            // Horizontal selectors for Occasions
            Text(
                text = "Selecciona una ocasión de uso:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B4E28),
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("outfit_selectors_row")
            ) {
                items(occasionsList) { occ ->
                    val isSelected = occ == selectedOccasion
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Color(0xFFA56600) else Color(0xFFF0EAE1))
                            .clickable {
                                selectedOccasion = occ
                                successSaveBanner = false
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("btn_select_occ_$occ"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = occ,
                            color = if (isSelected) Color.White else Color(0xFF5C4F43),
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Suggestion Card outcome
            if (topItem == null && shoesItem == null) {
                // Empty wardrobe placeholder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCD34D))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Checkroom, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aún no tienes suficientes prendas en el armario para sugerir un outfit completo.",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            } else {
                // Displays coordinates in a grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Combinación Sugerida ($selectedOccasion)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A3E31),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Side-by-side or stacked visual cards of the garment elements
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth().testTag("suggested_outfit_board")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Row containing top & bottom jackets
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Torso Card
                                topItem?.let { item ->
                                    OutfitComponentDisplay(
                                        label = "Prenda Superior",
                                        item = item,
                                        modifier = Modifier.weight(1f).testTag("preview_outfit_top")
                                    )
                                }

                                // Legs Card (Only if dress is not covering both)
                                if (bottomItem != null) {
                                    OutfitComponentDisplay(
                                        label = "Prenda Inferior",
                                        item = bottomItem!!,
                                        modifier = Modifier.weight(1f).testTag("preview_outfit_bottom")
                                    )
                                } else if (topItem?.categoria == "Vestidos") {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(110.dp)
                                            .background(Color(0xFFF9F7F5), RoundedCornerShape(12.dp))
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "La prenda superior es un vestido (Outfit completo o de una pieza).",
                                            fontSize = 11.sp,
                                            textAlign = TextAlign.Center,
                                            color = Color(0xFF8C755E)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Shoes Card
                                shoesItem?.let { item ->
                                    OutfitComponentDisplay(
                                        label = "Calzado",
                                        item = item,
                                        modifier = Modifier.weight(1f).testTag("preview_outfit_shoes")
                                    )
                                }

                                // Accessories/Suggested Card
                                accItem?.let { item ->
                                    OutfitComponentDisplay(
                                        label = "Accesorio sugerido",
                                        item = item,
                                        modifier = Modifier.weight(1f).testTag("preview_outfit_acc")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = Color(0xFFF0EAE1))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Smart Harmony Tip Box
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFCFAF7), RoundedCornerShape(10.dp))
                                    .padding(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Idea",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = suggestionLog,
                                    fontSize = 12.sp,
                                    color = Color(0xFF5C4F43),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (successSaveBanner) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFD1FAE5)),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF059669))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("¡Combinación guardada con éxito!", fontSize = 13.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Bottom: Save Outfit combination button
                    Button(
                        onClick = {
                            viewModel.saveOutfit(
                                nombreOutfit = selectedOccasion,
                                top = topItem,
                                bottom = bottomItem,
                                shoes = shoesItem,
                                acc = accItem
                            )
                            successSaveBanner = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_save_outfit_assembly"),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B4E28),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar esta Combinación", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // LIST PREVIOUS SAVED OUTFITS
            Divider(color = Color(0xFFF0EAE1))
            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Combinaciones Guardadas (${savedOutfits.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A3E31),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (savedOutfits.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFF0EAE1), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ninguna combinación guardada todavía.",
                            fontSize = 13.sp,
                            color = Color(0xFF8C755E)
                        )
                    }
                } else {
                    savedOutfits.forEach { outfit ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                                .testTag("saved_outfit_card_${outfit.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color(0xFFF5EFEB), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color(0xFFA56600),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Estilo ${outfit.nombreOutfit}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4A3E31)
                                        )
                                        Text(
                                            text = "Conjunto de prendas digitales coordinadas",
                                            fontSize = 11.sp,
                                            color = Color(0xFF8C755E)
                                        )
                                    }
                                }

                                // Delete outfit button
                                IconButton(
                                    onClick = { viewModel.deleteOutfit(outfit) },
                                    modifier = Modifier.testTag("btn_delete_outfit_${outfit.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Saved Outfit",
                                        tint = Color.Gray
                                    )
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
fun OutfitComponentDisplay(
    label: String,
    item: GarmentEntity,
    modifier: Modifier = Modifier
) {
    val chipColor = try {
        Color(android.graphics.Color.parseColor(item.color))
    } catch (e: Exception) {
        Color(0xFFA56600)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCF9)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EAE1)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF8C755E),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                contentAlignment = Alignment.Center
            ) {
                ApparelGraphic(
                    imageName = item.imagen,
                    garmentColor = chipColor,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.nombre,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C4731),
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Text(
                text = item.colorName,
                fontSize = 10.sp,
                color = Color(0xFF8C755E)
            )
        }
    }
}
