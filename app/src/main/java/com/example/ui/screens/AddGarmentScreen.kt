package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ApparelGraphic
import com.example.ui.viewmodel.ClosetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGarmentScreen(
    viewModel: ClosetViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    
    // Categorías selection
    val categories = listOf("Camisas", "Camisetas", "Pantalones", "Chaquetas", "Vestidos", "Zapatos", "Joyas", "Accesorios", "Bolsos")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }

    // Color options
    val colorsList = listOf(
        ColorOption("Marrón", "#8B5A2B", Color(0xFF8B5A2B)),
        ColorOption("Beige", "#E8DCCA", Color(0xFFE8DCCA)),
        ColorOption("Blanco", "#FFFFFF", Color(0xFFFFFFFF)),
        ColorOption("Negro", "#000000", Color(0xFF000000)),
        ColorOption("Azul", "#3B82F6", Color(0xFF3B82F6)),
        ColorOption("Celeste", "#06B6D4", Color(0xFF06B6D4)),
        ColorOption("Rojo", "#EF4444", Color(0xFFEF4444)),
        ColorOption("Amarillo", "#EAB308", Color(0xFFEAB308)),
        ColorOption("Verde", "#22C55E", Color(0xFF22C55E)),
        ColorOption("Rosa", "#EC4899", Color(0xFFEC4899))
    )
    var selectedColor by remember { mutableStateOf(colorsList[0]) }

    // Temporadas
    val seasons = listOf("Primavera", "Verano", "Otoño", "Invierno")
    var selectedSeason by remember { mutableStateOf(seasons[0]) }
    var seasonExpanded by remember { mutableStateOf(false) }

    // Ocasiones
    val occasions = listOf("Casual", "Formal", "Deportivo", "Elegante", "Fiesta", "Oficina")
    var selectedOccasion by remember { mutableStateOf(occasions[0]) }
    var occasionExpanded by remember { mutableStateOf(false) }

    // Graphics presets to look beautifully on Canvas
    val graphicPresets = mapOf(
        "Camisas" to "shirt_blue",
        "Camisetas" to "t_shirt_beige",
        "Pantalones" to "pants_tan",
        "Chaquetas" to "jacket_leather",
        "Vestidos" to "dress_elegant",
        "Zapatos" to "shoes_loafer",
        "Bolsos" to "bag_leather",
        "Accesorios" to "acc_glasses",
        "Joyas" to "jewelry_watch"
    )
    val chosenGraphic = graphicPresets[selectedCategory] ?: "t_shirt_beige"

    // Mock Camera Overlay Flow
    var showCameraOverlay by remember { mutableStateOf(false) }
    var cameraShutterClicked by remember { mutableStateOf(false) }
    var cameraFlashState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Prenda 👕", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("add_garment_back")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF6B4E28))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFDFBF7))
            )
        },
        containerColor = Color(0xFFFDFBF7)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive Garment preview card
                Card(
                    modifier = Modifier
                        .size(170.dp)
                        .testTag("garment_preview_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ApparelGraphic(
                            imageName = chosenGraphic,
                            garmentColor = selectedColor.color,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Photos Actions Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showCameraOverlay = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0EAE1), contentColor = Color(0xFF6B4E28)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("btn_camera_mock")
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tomar Foto", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            name = "Prenda de Galería " + (1..100).random().toString()
                            brand = "H&M"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0EAE1), contentColor = Color(0xFF6B4E28)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("btn_gallery_mock")
                    ) {
                        Icon(imageVector = Icons.Default.PhotoAlbum, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Galería", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Inputs form
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Prenda") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFA56600),
                        unfocusedBorderColor = Color(0xFFD3C2AF),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_garment_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Exposed dropdown category selector
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("dropdown_category")
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, color = Color(0xFF4A3E31)) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                },
                                modifier = Modifier.testTag("dropdown_item_$category")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Selection chips scroll
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Color Principal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4E28),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier.fillMaxWidth().testTag("color_selectors")
                    ) {
                        items(colorsList) { item ->
                            val isSelected = item.hex == selectedColor.hex
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(item.color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFA56600) else Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = item }
                                    .testTag("color_chip_${item.name}"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = if (item.name == "Blanco" || item.name == "Beige") Color.Black else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Input
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFA56600),
                        unfocusedBorderColor = Color(0xFFD3C2AF),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_garment_brand")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Temporada selector
                ExposedDropdownMenuBox(
                    expanded = seasonExpanded,
                    onExpandedChange = { seasonExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedSeason,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Temporada") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = seasonExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("dropdown_season")
                    )
                    ExposedDropdownMenu(
                        expanded = seasonExpanded,
                        onDismissRequest = { seasonExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        seasons.forEach { season ->
                            DropdownMenuItem(
                                text = { Text(season, color = Color(0xFF4A3E31)) },
                                onClick = {
                                    selectedSeason = season
                                    seasonExpanded = false
                                },
                                modifier = Modifier.testTag("season_item_$season")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ocasión selector
                ExposedDropdownMenuBox(
                    expanded = occasionExpanded,
                    onExpandedChange = { occasionExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedOccasion,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ocasión de Uso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = occasionExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("dropdown_occasion")
                    )
                    ExposedDropdownMenu(
                        expanded = occasionExpanded,
                        onDismissRequest = { occasionExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        occasions.forEach { occasion ->
                            DropdownMenuItem(
                                text = { Text(occasion, color = Color(0xFF4A3E31)) },
                                onClick = {
                                    selectedOccasion = occasion
                                    occasionExpanded = false
                                },
                                modifier = Modifier.testTag("occasion_item_$occasion")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Guardar Prenda Submit Button
                Button(
                    onClick = {
                        val finalName = if (name.isBlank()) "Prenda de ${selectedColor.name}" else name
                        val finalBrand = if (brand.isBlank()) "Sin marca" else brand
                        viewModel.addGarment(
                            nombre = finalName,
                            categoria = selectedCategory,
                            colorHex = selectedColor.hex,
                            colorName = selectedColor.name,
                            imagePath = chosenGraphic,
                            marca = finalBrand,
                            temporada = selectedSeason,
                            ocasion = selectedOccasion
                        )
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("add_garment_btn_submit"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA56600),
                        contentColor = Color.White
                    )
                ) {
                    Text("Guardar Prenda 👕", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            // MOCK CAMERA INTERACTIVE MODAL OVERLAY
            AnimatedVisibility(
                visible = showCameraOverlay,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.95f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Apertura de Cámara 📷",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Camera Viewfinder Box
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                                .background(Color(0xFF2C2520)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (cameraFlashState) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White)
                                )
                            }
                            
                            // Draws a retro chic wireframe preview of selected apparel!
                            ApparelGraphic(
                                imageName = chosenGraphic,
                                garmentColor = selectedColor.color,
                                modifier = Modifier
                                    .size(200.dp)
                                    .testTag("camera_view_graphic")
                            )

                            // Alignment crosses
                            Box(modifier = Modifier.size(54.dp).border(1.dp, Color.White.copy(alpha = 0.5f)))
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Large Shutter Snap Button
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable {
                                    cameraFlashState = true
                                    cameraShutterClicked = true
                                    // Flash effect
                                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                        cameraFlashState = false
                                        showCameraOverlay = false
                                        name = "${selectedCategory} ${selectedColor.name} Snap"
                                        brand = "Foto de Cámara"
                                    }, 250)
                                }
                                .padding(4.dp)
                                .border(3.dp, Color.Black, CircleShape)
                                .testTag("btn_camera_capture_shutter"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Capturar",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        TextButton(
                            onClick = { showCameraOverlay = false }
                        ) {
                            Text("Cancelar", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}

data class ColorOption(
    val name: String,
    val hex: String,
    val color: Color
)
