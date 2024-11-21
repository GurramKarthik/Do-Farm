package com.example.doform.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.doform.dataClass.Crop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CropViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CropRepository

    init {
        val appDAO = DofarmDatabase.getDatabase(application).appDAO()
        repository = CropRepository(appDAO)
    }

    fun addCrop(crop: Crops){
        viewModelScope.launch (Dispatchers.IO) {
            repository.insertCrop(crop)
        }
    }

    suspend fun getCropsByUser(userId: Int) : LiveData<List<Crops>> {
        return repository.getCropsByUser(userId)
    }

    suspend fun getAllCrops() : LiveData<List<Crops>> {
        return repository.getAllCrops()
    }

    suspend fun getCropsById(cropId: Int) : LiveData<Crops> {
        return repository.getCropById(cropId)
    }

    suspend fun updateCrop(cropId: Int, newQuantity: Int, newPrice: Double) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateCrop(cropId, newQuantity, newPrice)
        }
    }

    suspend fun updateCropStatus(cropId: Int, newStatus: String, buyerName: String, buyerPhone: String) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.updateCropStatus(cropId, newStatus, buyerName ,buyerPhone)
        }
    }

    suspend fun deleteCropById(cropId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteCropById(cropId)
        }
    }


}