package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SmartClosetLogo
import com.example.ui.viewmodel.AuthUiState
import com.example.ui.viewmodel.ClosetViewModel

@Composable
fun SignUpScreen(
    viewModel: ClosetViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authUiState.collectAsState()

    // Handle registration outcome
    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8DCCA), // Soil / Wood warm elements
                        Color(0xFFF5F0E6),
                        Color(0xFF8B5A2B)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SmartClosetLogo(textColor = Color(0xFF6B4E28))

            Spacer(modifier = Modifier.height(20.dp))

            // White elegant card with rounded corners
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signup_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Crear Cuenta",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B4E28),
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    if (authState is AuthUiState.Error) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error message icon",
                                    tint = Color.Red,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = (authState as AuthUiState.Error).message,
                                    fontSize = 13.sp,
                                    color = Color(0xFF991B1B)
                                )
                            }
                        }
                    }

                    // Nome input
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            viewModel.clearAuthError()
                        },
                        label = { Text("Nombre") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFF8C755E))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedLabelColor = Color(0xFFA56600),
                            unfocusedLabelColor = Color(0xFF8C755E)
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("nombre_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Apellido input
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = {
                            apellido = it
                            viewModel.clearAuthError()
                        },
                        label = { Text("Apellido") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFF8C755E))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedLabelColor = Color(0xFFA56600),
                            unfocusedLabelColor = Color(0xFF8C755E)
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apellido_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email input
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            viewModel.clearAuthError()
                        },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFF8C755E))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedLabelColor = Color(0xFFA56600),
                            unfocusedLabelColor = Color(0xFF8C755E)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_email_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password input
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            viewModel.clearAuthError()
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFF8C755E))
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF8C755E)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedLabelColor = Color(0xFFA56600),
                            unfocusedLabelColor = Color(0xFF8C755E)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_password_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Confirm password input
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            viewModel.clearAuthError()
                        },
                        label = { Text("Confirmar Contraseña") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFF8C755E))
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF8C755E)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFA56600),
                            unfocusedBorderColor = Color(0xFFD3C2AF),
                            focusedLabelColor = Color(0xFFA56600),
                            unfocusedLabelColor = Color(0xFF8C755E)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_confirm_password_input")
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Submitbutton
                    if (authState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color(0xFFA56600),
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Button(
                            onClick = {
                                viewModel.register(nombre, apellido, email, contrasena, confirmarContrasena)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("register_submit_button"),
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFA56600),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Crear Cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("back_to_login_button")
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta? Inicia sesión",
                            color = Color(0xFF8B5A2B),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
