package com.example.data.repository

import com.example.data.database.GarmentDao
import com.example.data.database.GarmentEntity
import com.example.data.database.OutfitDao
import com.example.data.database.OutfitEntity
import com.example.data.database.UserDao
import com.example.data.database.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ClosetRepository(
    private val userDao: UserDao,
    private val garmentDao: GarmentDao,
    private val outfitDao: OutfitDao
) {
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    fun getUserById(id: Int): Flow<UserEntity?> {
        return userDao.getUserById(id)
    }

    suspend fun registerUser(user: UserEntity): Long {
        val newUserId = userDao.insertUser(user)
        // Auto-populate with standard template clothes so the app has data immediately!
        preloadTemplatesForUser(newUserId.toInt())
        return newUserId
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    fun getGarmentsByUserId(userId: Int): Flow<List<GarmentEntity>> {
        return garmentDao.getGarmentsByUserId(userId)
    }

    suspend fun insertGarment(garment: GarmentEntity): Long {
        return garmentDao.insertGarment(garment)
    }

    suspend fun updateGarment(garment: GarmentEntity) {
        garmentDao.updateGarment(garment)
    }

    suspend fun deleteGarment(garment: GarmentEntity) {
        garmentDao.deleteGarment(garment)
    }

    fun getOutfitsByUserId(userId: Int): Flow<List<OutfitEntity>> {
        return outfitDao.getOutfitsByUserId(userId)
    }

    suspend fun insertOutfit(outfit: OutfitEntity): Long {
        return outfitDao.insertOutfit(outfit)
    }

    suspend fun deleteOutfit(outfit: OutfitEntity) {
        outfitDao.deleteOutfit(outfit)
    }

    private suspend fun preloadTemplatesForUser(userId: Int) {
        val existing = garmentDao.getGarmentsByUserId(userId).firstOrNull()?.size ?: 0
        if (existing == 0) {
            val templates = listOf(
                // Shirts & T-Shirts (Superior)
                GarmentEntity(
                    userId = userId,
                    nombre = "Camisa de Lino Arena",
                    categoria = "Camisas",
                    color = "#E6D7C3",
                    colorName = "Beige",
                    imagen = "t_shirt_beige",
                    marca = "Zara",
                    temporada = "Verano",
                    ocasion = "Casual"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Camiseta Café Intenso",
                    categoria = "Camisetas",
                    color = "#5C4033",
                    colorName = "Marrón",
                    imagen = "t_shirt_brown",
                    marca = "H&M",
                    temporada = "Verano",
                    ocasion = "Deportivo"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Camisa Formal Celeste",
                    categoria = "Camisas",
                    color = "#ADD8E6",
                    colorName = "Celeste",
                    imagen = "shirt_blue",
                    marca = "Mango",
                    temporada = "Primavera",
                    ocasion = "Oficina"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Camiseta Básica Blanca",
                    categoria = "Camisetas",
                    color = "#FFFFFF",
                    colorName = "Blanco",
                    imagen = "t_shirt_white",
                    marca = "Uniqlo",
                    temporada = "Verano",
                    ocasion = "Casual"
                ),
                // Dress
                GarmentEntity(
                    userId = userId,
                    nombre = "Vestido de Gala Marrón",
                    categoria = "Vestidos",
                    color = "#8B5A2B",
                    colorName = "Marrón",
                    imagen = "dress_elegant",
                    marca = "Massimo Dutti",
                    temporada = "Otoño",
                    ocasion = "Elegante"
                ),
                // Pants (Inferior)
                GarmentEntity(
                    userId = userId,
                    nombre = "Chino Beige Clásico",
                    categoria = "Pantalones",
                    color = "#D2B48C",
                    colorName = "Beige",
                    imagen = "pants_tan",
                    marca = "Levi's",
                    temporada = "Primavera",
                    ocasion = "Casual"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Pantalón Oxford Azul",
                    categoria = "Pantalones",
                    color = "#1F2937",
                    colorName = "Azul",
                    imagen = "pants_blue",
                    marca = "Zara",
                    temporada = "Invierno",
                    ocasion = "Oficina"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Pantalón Sastre Negro",
                    categoria = "Pantalones",
                    color = "#000000",
                    colorName = "Negro",
                    imagen = "pants_black",
                    marca = "Mango",
                    temporada = "Otoño",
                    ocasion = "Elegante"
                ),
                // Jackets
                GarmentEntity(
                    userId = userId,
                    nombre = "Chaqueta Cuero Terracota",
                    categoria = "Chaquetas",
                    color = "#C05C2E",
                    colorName = "Marrón",
                    imagen = "jacket_leather",
                    marca = "Zara",
                    temporada = "Otoño",
                    ocasion = "Fiesta"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Chaqueta Deportiva Azul",
                    categoria = "Chaquetas",
                    color = "#2563EB",
                    colorName = "Azul",
                    imagen = "jacket_sport",
                    marca = "Adidas",
                    temporada = "Primavera",
                    ocasion = "Deportivo"
                ),
                // Shoes
                GarmentEntity(
                    userId = userId,
                    nombre = "Mocasines de Cuero",
                    categoria = "Zapatos",
                    color = "#8B4513",
                    colorName = "Marrón",
                    imagen = "shoes_loafer",
                    marca = "Hugo Boss",
                    temporada = "Primavera",
                    ocasion = "Formal"
                ),
                GarmentEntity(
                    userId = userId,
                    nombre = "Sneakers Minimalistas",
                    categoria = "Zapatos",
                    color = "#FFFFFF",
                    colorName = "Blanco",
                    imagen = "shoes_sneaker",
                    marca = "Nike",
                    temporada = "Verano",
                    ocasion = "Deportivo"
                ),
                // Bags
                GarmentEntity(
                    userId = userId,
                    nombre = "Bolso Satchel de Cuero",
                    categoria = "Bolsos",
                    color = "#A52A2A",
                    colorName = "Marrón",
                    imagen = "bag_leather",
                    marca = "Purificación García",
                    temporada = "Primavera",
                    ocasion = "Oficina"
                ),
                // Accessories
                GarmentEntity(
                    userId = userId,
                    nombre = "Gafas de Sol Carey",
                    categoria = "Accesorios",
                    color = "#D2B48C",
                    colorName = "Dorado/Beige",
                    imagen = "acc_glasses",
                    marca = "Ray-Ban",
                    temporada = "Verano",
                    ocasion = "Casual"
                ),
                // Jewelry
                GarmentEntity(
                    userId = userId,
                    nombre = "Reloj Clásico de Acero",
                    categoria = "Joyas",
                    color = "#FFD700",
                    colorName = "Oro",
                    imagen = "jewelry_watch",
                    marca = "Rolex",
                    temporada = "Invierno",
                    ocasion = "Elegante"
                )
            )
            for (item in templates) {
                garmentDao.insertGarment(item)
            }
        }
    }
}
