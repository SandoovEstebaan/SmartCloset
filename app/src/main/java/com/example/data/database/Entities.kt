package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String,
    val edad: String = "25",
    val genero: String = "No especificado",
    val estiloPreferencia: String = "Casual",
    val fotoPerfil: String? = null // Store custom avatar uri or asset template name
)

@Entity(tableName = "garments")
data class GarmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Refers to UserEntity.id
    val nombre: String,
    val categoria: String, // Camisas, Camisetas, Pantalones, Chaquetas, Vestidos, Zapatos, Joyas, Accesorios, Bolsos
    val color: String, // Hex string like #A56600 or simple name
    val colorName: String = "Marrón", // Human readable name
    val imagen: String, // local template image ID or simulated camera image uri
    val marca: String,
    val temporada: String, // Primavera, Verano, Otoño, Invierno
    val ocasion: String, // Casual, Formal, Deportivo, Elegante, Fiesta, Oficina
    val esFavorito: Boolean = false
)

@Entity(tableName = "outfits")
data class OutfitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val nombreOutfit: String, // Casual, Formal, Deportivo, Elegante, Fiesta, Oficina
    val prendaSuperiorId: Int?,
    val prendaInferiorId: Int?,
    val zapatosId: Int?,
    val accesorioId: Int?
)
