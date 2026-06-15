package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.ClosetViewModel

data class DashboardMenuItem(
    val title: String,
    val icon: ImageVector,
    val iconColor: Color,
    val destination: String,
    val testTag: String
)

@Composable
fun DashboardScreen(
    viewModel: ClosetViewModel,
    onNavigate: (String) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val firstName = currentUser?.nombre ?: "Usuario"

    val menuItems = listOf(
        DashboardMenuItem(
            title = "Mi Armario",
            icon = Icons.Default.Checkroom,
            iconColor = Color(0xFFA56600),
            destination = "closet",
            testTag = "menu_btn_closet"
        ),
        DashboardMenuItem(
            title = "Outfits",
            icon = Icons.Default.AutoAwesome,
            iconColor = Color(0xFF6B4E28),
            destination = "outfits",
            testTag = "menu_btn_outfits"
        ),
        DashboardMenuItem(
            title = "Círculo Cromático",
            icon = Icons.Default.Brush,
            iconColor = Color(0xFF10B981),
            destination = "color_wheel",
            testTag = "menu_btn_color_wheel"
        ),
        DashboardMenuItem(
            title = "Favoritos",
            icon = Icons.Default.Favorite,
            iconColor = Color(0xFFEF4444),
            destination = "favorites",
            testTag = "menu_btn_favorites"
        ),
        DashboardMenuItem(
            title = "Perfil",
            icon = Icons.Default.Person,
            iconColor = Color(0xFF3B82F6),
            destination = "profile",
            testTag = "menu_btn_profile"
        ),
        DashboardMenuItem(
            title = "Configuración",
            icon = Icons.Default.Settings,
            iconColor = Color(0xFF718096),
            destination = "settings",
            testTag = "menu_btn_settings"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFDFBF7) // Soft elegant sand color
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Elegant Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF8B5A2B), Color(0xFF6B4E28))
                        ),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF5EFEB))
                            .clickable { onNavigate("profile") }
                            .testTag("avatar_container"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = firstName.take(1).uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B4E28)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Bienvenido,",
                            fontSize = 15.sp,
                            color = Color(0xFFE2D1C1)
                        )
                        Text(
                            text = firstName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.testTag("welcome_name_text")
                        )
                    }
                }
            }

            // Grid Section
            Text(
                text = "Tu Estilo Virtual",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C4F43),
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(menuItems.size) { index ->
                    val item = menuItems[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.1f)
                            .clickable { onNavigate(item.destination) }
                            .testTag(item.testTag),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(item.iconColor.copy(alpha = 0.12f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = "${item.title} icon",
                                    tint = item.iconColor,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5C4731),
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
            }
        }
    }
}
