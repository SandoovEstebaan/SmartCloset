package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.GarmentEntity
import com.example.data.database.OutfitEntity
import com.example.data.database.UserEntity
import com.example.data.repository.ClosetRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: UserEntity) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

// Representing 12 segments of the Color Wheel
data class ColorSegment(
    val name: String,
    val hexColor: String,
    val color: Color
)

class ClosetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClosetRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ClosetRepository(
            userDao = database.userDao(),
            garmentDao = database.garmentDao(),
            outfitDao = database.outfitDao()
        )
    }

    // Auth & Navigation States
    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _rememberSession = MutableStateFlow(true)
    val rememberSession: StateFlow<Boolean> = _rememberSession.asStateFlow()

    // Screen navigation triggers (Mocks)
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    // Observe user garments dynamically when logged in
    val userGarments: StateFlow<List<GarmentEntity>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getGarmentsByUserId(user.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Observe saved outfits dynamically
    val savedOutfits: StateFlow<List<OutfitEntity>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getOutfitsByUserId(user.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setRememberSession(remember: Boolean) {
        _rememberSession.value = remember
    }

    // 1. Initial Login Check (e.g. if we want to remember)
    fun attemptAutoLogin() {
        // Can be queried from datastore or DB (let's check a default mock if rememberSession is set)
        viewModelScope.launch {
            val defaultEmail = "estebanestudiante2@gmail.com"
            val existing = repository.getUserByEmail(defaultEmail)
            if (existing != null && _rememberSession.value) {
                _currentUser.value = existing
                _authUiState.value = AuthUiState.Success(existing)
            }
        }
    }

    // 2. Login Logic
    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            if (email.isBlank() || contrasena.isBlank()) {
                _authUiState.value = AuthUiState.Error("Por favor completa todos los campos.")
                return@launch
            }
            val user = repository.getUserByEmail(email)
            if (user == null) {
                _authUiState.value = AuthUiState.Error("Usuario no encontrado. Regístrate.")
            } else if (user.contrasena != contrasena) {
                _authUiState.value = AuthUiState.Error("Contraseña incorrecta.")
            } else {
                _currentUser.value = user
                _authUiState.value = AuthUiState.Success(user)
            }
        }
    }

    // 3. Signup Logic
    fun register(nombre: String, apellido: String, email: String, contrasena: String, confirmarContrasena: String) {
        viewModelScope.launch {
            _authUiState.value = AuthUiState.Loading
            if (nombre.isBlank() || apellido.isBlank() || email.isBlank() || contrasena.isBlank() || confirmarContrasena.isBlank()) {
                _authUiState.value = AuthUiState.Error("Todos los campos son obligatorios.")
                return@launch
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _authUiState.value = AuthUiState.Error("Introduce un correo electrónico válido.")
                return@launch
            }
            if (contrasena != confirmarContrasena) {
                _authUiState.value = AuthUiState.Error("Las contraseñas no coinciden.")
                return@launch
            }
            if (contrasena.length < 5) {
                _authUiState.value = AuthUiState.Error("La contraseña debe tener al menos 5 caracteres.")
                return@launch
            }

            val existing = repository.getUserByEmail(email)
            if (existing != null) {
                _authUiState.value = AuthUiState.Error("El correo electrónico ya está registrado.")
                return@launch
            }

            val newUser = UserEntity(
                nombre = nombre,
                apellido = apellido,
                correo = email,
                contrasena = contrasena
            )
            val insertedId = repository.registerUser(newUser)
            val createdUser = newUser.copy(id = insertedId.toInt())
            _currentUser.value = createdUser
            _authUiState.value = AuthUiState.Success(createdUser)
        }
    }

    fun logout() {
        _currentUser.value = null
        _authUiState.value = AuthUiState.Idle
    }

    // Update Profile
    fun updateProfile(nombre: String, apellido: String, edad: String, genero: String, estiloPreferencia: String, fotoPerfil: String?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(
                nombre = nombre,
                apellido = apellido,
                edad = edad,
                genero = genero,
                estiloPreferencia = estiloPreferencia,
                fotoPerfil = fotoPerfil
            )
            repository.updateUser(updated)
            _currentUser.value = updated
        }
    }

    fun clearAuthError() {
        if (_authUiState.value is AuthUiState.Error) {
            _authUiState.value = AuthUiState.Idle
        }
    }

    // 4. Garment Operations
    fun addGarment(
        nombre: String,
        categoria: String,
        colorHex: String,
        colorName: String,
        imagePath: String,
        marca: String,
        temporada: String,
        ocasion: String
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val garment = GarmentEntity(
                userId = user.id,
                nombre = nombre,
                categoria = categoria,
                color = colorHex,
                colorName = colorName,
                imagen = imagePath,
                marca = marca,
                temporada = temporada,
                ocasion = ocasion
            )
            repository.insertGarment(garment)
        }
    }

    fun toggleFavorite(garment: GarmentEntity) {
        viewModelScope.launch {
            repository.updateGarment(garment.copy(esFavorito = !garment.esFavorito))
        }
    }

    fun deleteGarment(garment: GarmentEntity) {
        viewModelScope.launch {
            repository.deleteGarment(garment)
        }
    }

    // 5. Saved Outfits
    fun saveOutfit(nombreOutfit: String, top: GarmentEntity?, bottom: GarmentEntity?, shoes: GarmentEntity?, acc: GarmentEntity?) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val outfit = OutfitEntity(
                userId = user.id,
                nombreOutfit = nombreOutfit,
                prendaSuperiorId = top?.id,
                prendaInferiorId = bottom?.id,
                zapatosId = shoes?.id,
                accesorioId = acc?.id
            )
            repository.insertOutfit(outfit)
        }
    }

    fun deleteOutfit(outfit: OutfitEntity) {
        viewModelScope.launch {
            repository.deleteOutfit(outfit)
        }
    }

    // COLOR WHEEL HARMONIES
    val colorWheelSegments = listOf(
        ColorSegment("Rojo", "#EF4444", Color(0xFFEF4444)),
        ColorSegment("Naranja", "#F97316", Color(0xFFF97316)),
        ColorSegment("Amarillo", "#EAB308", Color(0xFFEAB308)),
        ColorSegment("Verde Lima", "#84CC16", Color(0xFF84CC16)),
        ColorSegment("Verde", "#22C55E", Color(0xFF22C55E)),
        ColorSegment("Menta / Celeste", "#06B6D4", Color(0xFF06B6D4)),
        ColorSegment("Azul", "#3B82F6", Color(0xFF3B82F6)),
        ColorSegment("Índigo", "#6366F1", Color(0xFF6366F1)),
        ColorSegment("Violeta", "#8B5CF6", Color(0xFF8B5CF6)),
        ColorSegment("Rosa / Coral", "#EC4899", Color(0xFFEC4899)),
        ColorSegment("Marrón / Café", "#8B5A2B", Color(0xFF8B5A2B)),
        ColorSegment("Beige / Arena", "#F5F0E6", Color(0xFFF5F0E6))
    )

    private val _selectedColorIndex = MutableStateFlow(0)
    val selectedColorIndex: StateFlow<Int> = _selectedColorIndex.asStateFlow()

    fun selectColorIndex(index: Int) {
        _selectedColorIndex.value = index
    }

    // Derived Harmonies
    val harmonies: StateFlow<HarmonyResult> = _selectedColorIndex.map { index ->
        val selected = colorWheelSegments[index]

        // Complementary (Opposite side)
        val compIndex = (index + 6) % 12
        val comp = colorWheelSegments[compIndex]

        // Analogous (Neighbors)
        val anaLeftIndex = (index - 1 + 12) % 12
        val anaRightIndex = (index + 1) % 12
        val anaLeft = colorWheelSegments[anaLeftIndex]
        val anaRight = colorWheelSegments[anaRightIndex]

        // Triadic (Three equal-spaced colors)
        val triad1Index = (index + 4) % 12
        val triad2Index = (index + 8) % 12
        val triad1 = colorWheelSegments[triad1Index]
        val triad2 = colorWheelSegments[triad2Index]

        HarmonyResult(
            primary = selected,
            complementary = comp,
            analogous = listOf(anaLeft, anaRight),
            triadic = listOf(triad1, triad2)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HarmonyResult(colorWheelSegments[0], colorWheelSegments[6], emptyList(), emptyList()))

    // Suggest garments that match the harmonies
    fun retrieveSuggestedGarmentsByHarmonies(harmResult: HarmonyResult, allGarments: List<GarmentEntity>): List<GarmentEntity> {
        val harmonyColors = setOf(
            harmResult.primary.name.lowercase(),
            harmResult.complementary.name.lowercase(),
            harmResult.analogous.map { it.name.lowercase() }.getOrNull(0) ?: "",
            harmResult.analogous.map { it.name.lowercase() }.getOrNull(1) ?: "",
            harmResult.triadic.map { it.name.lowercase() }.getOrNull(0) ?: "",
            harmResult.triadic.map { it.name.lowercase() }.getOrNull(1) ?: ""
        )

        return allGarments.filter { garment ->
            val colorNameVal = garment.colorName.lowercase()
            val matchesSegment = harmonyColors.any { harmonyColor ->
                colorNameVal.contains(harmonyColor) || harmonyColor.contains(colorNameVal) ||
                        (colorNameVal == "marrón" && harmonyColor == "marrón / café") ||
                        (colorNameVal == "beige" && harmonyColor == "beige / arena") ||
                        (colorNameVal == "celeste" && harmonyColor == "menta / celeste")
            }
            matchesSegment
        }
    }
}

data class HarmonyResult(
    val primary: ColorSegment,
    val complementary: ColorSegment,
    val analogous: List<ColorSegment>,
    val triadic: List<ColorSegment>
)
