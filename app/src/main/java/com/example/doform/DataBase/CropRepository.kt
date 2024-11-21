package com.example.doform.DataBase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CropRepository(private val appDAO: AppDAO) {

    suspend fun insertCrop(crop: Crops) {
        appDAO.insertCrop(crop)
    }

    suspend fun getCropsByUser(userId: Int): LiveData<List<Crops>> {
        return withContext(Dispatchers.IO) {
             appDAO.getCropsByUser(userId)
        }
    }

    suspend fun getAllCrops(): LiveData<List<Crops>> {
        return withContext(Dispatchers.IO) {
            appDAO.getAllCrops()
        }
    }

    suspend fun getCropById(cropId: Int): LiveData<Crops> {
        return withContext(Dispatchers.IO) {
            appDAO.getCropById(cropId)
        }
    }


    suspend fun updateCrop(cropId: Int, newQuantity: Int, newPrice: Double) {
        appDAO.updateCrop(cropId, newQuantity, newPrice)
    }

    suspend fun updateCropStatus(cropId: Int, newStatus: String, buyerName: String, buyerPhone: String) {
        appDAO.updateCropStatus(cropId, newStatus, buyerName, buyerPhone)
    }

    suspend fun deleteCropById(cropId: Int) {
        appDAO.deleteCropById(cropId)
    }
}