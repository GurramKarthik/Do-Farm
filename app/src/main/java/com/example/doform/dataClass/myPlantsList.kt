package com.example.doform.dataClass

import android.graphics.Bitmap

data class myPlantsList(
    var cropImg: Bitmap?,
    var cropName: String,
    var quantity: Int,
    var price: Int,
    var status: String,
    var buyerName : String,
    var buyerPhone : String
){

}