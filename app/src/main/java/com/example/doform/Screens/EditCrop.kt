package com.example.doform.Screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.doform.DataBase.CropViewModel
import com.example.doform.R
import kotlinx.coroutines.launch

class EditCrop : AppCompatActivity() {
    private lateinit var mCropViewModel: CropViewModel
    lateinit var editQuantity : EditText
    lateinit var editPrice : EditText
    lateinit var updateBtn : Button
    lateinit var cropImage : ImageView
    lateinit var cropName : TextView
    var cropId = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_crop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cropId = intent.getIntExtra("cropId", -1)
        editQuantity = findViewById<EditText>(R.id.editQuantity)
        editPrice = findViewById<EditText>(R.id.editPrice)
        updateBtn = findViewById<Button>(R.id.updateBtn)
        cropImage = findViewById(R.id.cImage)
        cropName = findViewById(R.id.crName)
        mCropViewModel = ViewModelProvider(this).get(CropViewModel::class.java)
        lifecycleScope.launch {
            // Call the suspend function
            mCropViewModel.getCropsById(cropId).observe(this@EditCrop, Observer { crop ->
                if (crop != null) {
                    // Update UI with the fetched user data
                    var bitmap = byteArrayToBitmap(crop.cropImg)
                    cropImage.setImageBitmap(bitmap)
                    cropName.text = crop.cropName
                    editQuantity.setText(crop.quantity.toString())
                    editPrice.setText(crop.price.toString())
                }
            })
        }
        updateBtn.setOnClickListener {
            var quality = editQuantity.text.toString()
            var price = editPrice.text.toString()
            var isValid = validate(quality, price)
            if(isValid){
                update(cropId, quality.toInt(), price.toDouble());
                Toast.makeText(this, "Crop Updated", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Enter valid data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun update(cropId: Int, newQuantity: Int, newPrice: Double){
        lifecycleScope.launch {
            mCropViewModel.updateCrop(
                 cropId,
                newQuantity,
                newPrice
            )
            Toast.makeText(this@EditCrop, "Crop Updated Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun validate(   quality : String,  price : String ) : Boolean{
        var isValid = true

        if (quality.isBlank()) {
            editQuantity.error = "Quality cannot be empty"
            isValid = false
        } else if (quality.toIntOrNull() == null) {
            editQuantity.error = "Quality must be a valid number"
            isValid = false
        }

        if (price.isBlank()) {
            editPrice.error = "Price cannot be empty"
            isValid = false
        } else if (price.toDoubleOrNull() == null) {
            editPrice.error = "Price must be a valid number"
            isValid = false
        }
        return isValid
    }

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
    }
}