package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ClosetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: ClosetViewModel,
    onNavigateBack: () -> Unit
) {
    val rememberSession by viewModel.rememberSession.collectAsState()
    
    var darkThemeOverride by remember { mutableStateOf(false) }
    var notificationState by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración ⚙️", fontWeight = FontWeight.Bold, color = Color(0xFF6B4E28)) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("settings_btn_back")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Preferencia del Sistema",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3E31)
            )

            // Settings options items
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Remember User Session Switch
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = Color(0xFF8C755E))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Recordar Iniciar Sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A3E31))
                                Text("Evita pedir tu contraseña cada vez.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = rememberSession,
                            onCheckedChange = { viewModel.setRememberSession(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFA56600)),
                            modifier = Modifier.testTag("setting_switch_remember")
                        )
                    }

                    // Simulated Dark Mode support
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFF8C755E))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Modo Oscuro Adaptativo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A3E31))
                                Text("Usa fondos oscuros para conservar batería.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = darkThemeOverride,
                            onCheckedChange = { darkThemeOverride = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFA56600)),
                            modifier = Modifier.testTag("setting_switch_dark_mode")
                        )
                    }

                    // Notifications Switch
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFF8C755E))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Recordatorios de Outfits", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A3E31))
                                Text("Sugerencias de combinación cada mañana.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = notificationState,
                            onCheckedChange = { notificationState = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFA56600)),
                            modifier = Modifier.testTag("setting_switch_notifications")
                        )
                    }
                }
            }

            Text(
                text = "Información del Armario",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3E31)
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.BugReport, contentDescription = null, tint = Color(0xFF8C755E), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Versión: SmartCloset v1.0.0 (Kotlin + Compose)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5C4F43)
                        )
                    }
                    Text(
                        text = "Esta aplicación utiliza un almacenamiento local totalmente encriptado mediante SQLite y Room. Todas tus prendas y outfits se guardan físicamente en tu dispositivo Android para preservar tu privacidad de manera absoluta.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
