package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE correo = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface GarmentDao {
    @Query("SELECT * FROM garments WHERE userId = :userId ORDER BY id DESC")
    fun getGarmentsByUserId(userId: Int): Flow<List<GarmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGarment(garment: GarmentEntity): Long

    @Update
    suspend fun updateGarment(garment: GarmentEntity)

    @Delete
    suspend fun deleteGarment(garment: GarmentEntity)
}

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfits WHERE userId = :userId ORDER BY id DESC")
    fun getOutfitsByUserId(userId: Int): Flow<List<OutfitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: OutfitEntity): Long

    @Delete
    suspend fun deleteOutfit(outfit: OutfitEntity)
}
