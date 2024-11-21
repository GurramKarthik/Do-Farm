package com.example.doform.DataBase


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
public abstract interface AppDAO {
    // User Operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user:User)

    @Query("SELECT * FROM user_table  WHERE email = :email and password = :password and userType = :userType")
    fun getUser(email: String, password: String, userType: String): User?

    @Query("SELECT * FROM user_table WHERE userId = :id")
    fun getUserById(id: Int): LiveData<User>



    // Crop Operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrop(crop: Crops)

    @Query("SELECT * FROM crops_table WHERE userOwnerId = :userId")
    fun getCropsByUser(userId: Int): LiveData<List<Crops>>

    @Query("SELECT * FROM crops_table WHERE cropId = :cropId")
    fun getCropById(cropId: Int): LiveData<Crops>

    @Query("SELECT * FROM crops_table")
    fun getAllCrops(): LiveData<List<Crops>>

    // only for farmer
    @Query("UPDATE crops_table SET quantity = :newQuantity, price = :newPrice WHERE cropId = :cropId")
    suspend fun updateCrop(cropId: Int, newQuantity: Int, newPrice: Double)

    // when client buys the crop
    @Query("UPDATE crops_table SET status = :newStatus, oppositeName = :buyerName, oppositePhone = :buyerPhone WHERE cropId = :cropId")
    suspend fun updateCropStatus(cropId: Int, newStatus: String, buyerName: String, buyerPhone: String)

    // delete
    @Query("DELETE FROM crops_table WHERE cropId = :cropId")
    suspend fun deleteCropById(cropId: Int)
}
