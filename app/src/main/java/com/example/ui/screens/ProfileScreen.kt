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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Style
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.UserEntity
import com.example.ui.viewmodel.ClosetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ClosetViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    // Forms fields state
    var editNombre by remember { mutableStateOf(currentUser?.nombre ?: "") }
    var editApellido by remember { mutableStateOf(currentUser?.apellido ?: "") }
    var editEdad by remember { mutableStateOf(currentUser?.edad ?: "25") }
    var editGenero by remember { mutableStateOf(currentUser?.genero ?: "No especificado") }
    
    val stylesList = listOf("Casual", "Formal", "Deportivo", "Elegante", "Bohemio", "Urbano")
    var editEstilo by remember { mutableStateOf(currentUser?.estiloPreferencia ?: "Casual") }
    var StyleExpanded by remember { mutableStateOf(false) }

    // Avatars preset
    val avatarPresets = listOf("Estilo 1", "Estilo 2", "Estilo 3", "Estilo 4")
    var selectedAvatar by remember { mutableStateOf(currentUser?.fotoPerfil ?: avatarPresets[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil 👤", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("profile_btn_back")
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color(0xFF6B4E28))
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8DCCA)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (currentUser?.nombre?.take(1)?.uppercase() ?: "S"),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B4E28)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${currentUser?.nombre} ${currentUser?.apellido}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3E31)
            )

            Text(
                text = currentUser?.correo ?: "",
                fontSize = 13.sp,
                color = Color(0xFF8C755E)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile editable cards
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("profile_details_card")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Detalles Personales",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B4E28)
                        )

                        IconButton(
                            onClick = { isEditing = !isEditing },
                            modifier = Modifier.testTag("btn_toggle_edit_profile")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color(0xFFA56600),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isEditing) {
                        // Display Read-Only Profile Mode
                        ProfileFieldDisplay(
                            icon = Icons.Default.Person,
                            label = "Nombre Completo",
                            value = "${currentUser?.nombre} ${currentUser?.apellido}"
                        )

                        ProfileFieldDisplay(
                            icon = Icons.Default.Email,
                            label = "Correo de Cuenta",
                            value = currentUser?.correo ?: ""
                        )

                        ProfileFieldDisplay(
                            icon = Icons.Default.Info,
                            label = "Edad",
                            value = "${currentUser?.edad ?: "25"} años"
                        )

                        ProfileFieldDisplay(
                            icon = Icons.Default.Info,
                            label = "Género",
                            value = currentUser?.genero ?: "No especificado"
                        )

                        ProfileFieldDisplay(
                            icon = Icons.Default.Style,
                            label = "Preferencia de Estilo",
                            value = currentUser?.estiloPreferencia ?: "Casual"
                        )

                    } else {
                        // Display editable profiles fields
                        OutlinedTextField(
                            value = editNombre,
                            onValueChange = { editNombre = it },
                            label = { Text("Nombre") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA56600),
                                unfocusedBorderColor = Color(0xFFD3C2AF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_field_nombre")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editApellido,
                            onValueChange = { editApellido = it },
                            label = { Text("Apellido") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA56600),
                                unfocusedBorderColor = Color(0xFFD3C2AF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_field_apellido")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editEdad,
                            onValueChange = { editEdad = it },
                            label = { Text("Edad") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA56600),
                                unfocusedBorderColor = Color(0xFFD3C2AF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_field_edad")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = editGenero,
                            onValueChange = { editGenero = it },
                            label = { Text("Género") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFA56600),
                                unfocusedBorderColor = Color(0xFFD3C2AF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("edit_field_genero")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Exposed dropdown for styling preference
                        ExposedDropdownMenuBox(
                            expanded = StyleExpanded,
                            onExpandedChange = { StyleExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = editEstilo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Preferencia de Estilo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = StyleExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFA56600),
                                    unfocusedBorderColor = Color(0xFFD3C2AF)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("edit_dropdown_style")
                            )
                            ExposedDropdownMenu(
                                expanded = StyleExpanded,
                                onDismissRequest = { StyleExpanded = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                stylesList.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s, color = Color(0xFF4A3E31)) },
                                        onClick = {
                                            editEstilo = s
                                            StyleExpanded = false
                                        },
                                        modifier = Modifier.testTag("style_item_$s")
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                viewModel.updateProfile(
                                    nombre = editNombre,
                                    apellido = editApellido,
                                    edad = editEdad,
                                    genero = editGenero,
                                    estiloPreferencia = editEstilo,
                                    fotoPerfil = selectedAvatar
                                )
                                isEditing = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_save_profile_changes"),
                            shape = RoundedCornerShape(23.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA56600),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Cerrar Sesión Button
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("profile_btn_logout"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileFieldDisplay(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFFDFBF7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF8C755E), modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(text = label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 14.sp, color = Color(0xFF4A3E31), fontWeight = FontWeight.Bold)
        }
    }
}
