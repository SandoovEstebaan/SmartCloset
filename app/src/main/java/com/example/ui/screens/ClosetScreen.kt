package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.GarmentEntity
import com.example.ui.components.ApparelGraphic
import com.example.ui.viewmodel.ClosetViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ClosetScreen(
    viewModel: ClosetViewModel,
    filterOnlyFavorites: Boolean = false,
    onNavigateBack: () -> Unit,
    onNavigateToAddGarment: () -> Unit
) {
    val garments by viewModel.userGarments.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var garmentToDelete by remember { mutableStateOf<GarmentEntity?>(null) }

    val categories = listOf("Todos", "Camisas", "Camisetas", "Pantalones", "Chaquetas", "Vestidos", "Zapatos", "Joyas", "Accesorios", "Bolsos")

    // Filter garments
    val filteredGarments = garments.filter { garment ->
        val matchesSearch = garment.nombre.contains(searchQuery, ignoreCase = true) || 
                            garment.marca.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "Todos" || garment.categoria == selectedCategory
        val matchesFavorite = !filterOnlyFavorites || garment.esFavorito
        matchesSearch && matchesCategory && matchesFavorite
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (filterOnlyFavorites) "Mis Favoritos ❤️" else "Mi Armario 👕",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4E28)
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("closet_btn_back")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF6B4E28))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFDFBF7)),
                modifier = Modifier.testTag("closet_top_bar")
            )
        },
        floatingActionButton = {
            if (!filterOnlyFavorites) {
                FloatingActionButton(
                    onClick = onNavigateToAddGarment,
                    containerColor = Color(0xFFA56600),
                    contentColor = Color.White,
                    modifier = Modifier.testTag("closet_fab_add")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Apparel")
                }
            }
        },
        containerColor = Color(0xFFFDFBF7)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar prenda o marca...", color = Color(0xFF8C755E)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF8C755E)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFA56600),
                    unfocusedBorderColor = Color(0xFFD3C2AF),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("closet_search_bar")
            )

            // Category Filter Row
            if (!filterOnlyFavorites) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("closet_category_filters")
                ) {
                    items(categories.size) { index ->
                        val cat = categories[index]
                        val isSelected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Color(0xFFA56600) else Color(0xFFF0EAE1))
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("cat_filter_$cat"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) Color.White else Color(0xFF5C4F43),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Grid displaying filtered garments
            if (filteredGarments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = Color(0xFFD3C2AF),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No se encontraron prendas para \"$searchQuery\"" else "Tu armario está vacío.",
                            color = Color(0xFF8C755E),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .testTag("closet_grid")
                ) {
                    items(filteredGarments, key = { it.id }) { garment ->
                        val parsedColor = try {
                            Color(android.graphics.Color.parseColor(garment.color))
                        } catch (e: Exception) {
                            Color(0xFFA56600)
                        }

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = { garmentToDelete = garment }
                                )
                                .testTag("garment_card_${garment.id}")
                        ) {
                            Column {
                                // Draw dynamic vector asset using the custom canvas drawer
                                ApparelGraphic(
                                    imageName = garment.imagen,
                                    garmentColor = parsedColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                )

                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = garment.nombre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF4A3E31),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        // Favorite button
                                        IconButton(
                                            onClick = { viewModel.toggleFavorite(garment) },
                                            modifier = Modifier.size(24.dp).testTag("fav_btn_${garment.id}")
                                        ) {
                                            Icon(
                                                imageVector = if (garment.esFavorito) Icons.Default.Favorite else Icons.Filled.FavoriteBorder,
                                                contentDescription = "Toggle favorite",
                                                tint = Color.Red,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Display category and metadata
                                    Text(
                                        text = "${garment.categoria} • ${garment.marca}",
                                        color = Color(0xFF8C755E),
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Color badge row
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(parsedColor)
                                                .border(0.5.dp, Color.Gray, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = garment.colorName,
                                            color = Color(0xFF5C4F43),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    garmentToDelete?.let { garment ->
        AlertDialog(
            onDismissRequest = { garmentToDelete = null },
            title = { Text("Eliminar Prenda", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
            text = { Text("¿Estás seguro de que deseas eliminar permanentemente \"${garment.nombre}\" de tu armario virtual?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteGarment(garment)
                        garmentToDelete = null
                    },
                    modifier = Modifier.testTag("dialog_confirm_delete")
                ) {
                    Text("Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { garmentToDelete = null },
                    modifier = Modifier.testTag("dialog_cancel_delete")
                ) {
                    Text("Cancelar", color = Color(0xFF8C755E))
                }
            },
            containerColor = Color.White
        )
    }
}
