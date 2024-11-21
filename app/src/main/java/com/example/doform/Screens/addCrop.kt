package com.example.doform.Screens

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.doform.DataBase.CropViewModel
import com.example.doform.DataBase.Crops
import com.example.doform.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

class addCrop : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_GALLERY = 100
    }
    private lateinit var mCropViewModel: CropViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var addNewCropBtn: Button
    private lateinit var newCropName: EditText
    private lateinit var addQuality: EditText
    private lateinit var addPrice: EditText
    private lateinit var addImage: Button
    private lateinit var newCropImage: ImageView
    private lateinit var addUnits: Spinner
    private var selectedUnit: String = ""
    private var byteArray: ByteArray? = null
    private var userId: Int = 0
    private var userName: String = ""
    private var userPhone: String = ""
    private var userLocation = " ";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_crop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize UI components
        addNewCropBtn = findViewById(R.id.addNewCropBtn)
        newCropName = findViewById(R.id.newCropName)
        addQuality = findViewById(R.id.addQuality)
        addPrice = findViewById(R.id.addPrice)
        addImage = findViewById(R.id.addImage)
        newCropImage = findViewById(R.id.newCropImage)
        addUnits = findViewById(R.id.addUnits)
        mCropViewModel = ViewModelProvider(this).get(CropViewModel::class.java)
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val UID = sharedPreferences.getString("userUID", null)
        userId = UID?.toInt() ?: 0
        userName = sharedPreferences.getString("userName", "").toString()
        userPhone = sharedPreferences.getString("userPhone", "").toString()
        userLocation = sharedPreferences.getString("userLocation", "").toString();


        // Setting up the spinner
        val unitsAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf("KG", "Bags")
        )

        addUnits.adapter = unitsAdapter
        addUnits.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedUnit = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        // Setting up the image picker
        addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }

        addNewCropBtn.setOnClickListener {
            val isValid = validate(newCropName.text.toString(), addQuality.text.toString(), addPrice.text.toString())
            if (isValid) {
                addCropToRoom(newCropName.text.toString(), addQuality.text.toString(), addPrice.text.toString())
            } else {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            newCropImage.setImageURI(selectedImageUri)
            selectedImageUri?.let { uri ->
                lifecycleScope.launch {
                    byteArray = compressAndConvertUriToByteArray(uri)
                }
            }
        }
    }

    private fun validate(name: String, quality: String, price: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            newCropName.error = "Name cannot be empty"
            isValid = false
        }

        if (quality.isBlank()) {
            addQuality.error = "Quality cannot be empty"
            isValid = false
        } else if (quality.toIntOrNull() == null) {
            addQuality.error = "Quality must be a valid number"
            isValid = false
        }

        if (price.isBlank()) {
            addPrice.error = "Price cannot be empty"
            isValid = false
        } else if (price.toDoubleOrNull() == null) {
            addPrice.error = "Price must be a valid number"
            isValid = false
        }

        return isValid
    }

    private fun addCropToRoom(name: String, quality: String, price: String) {
        val time = System.currentTimeMillis()
        val newCrop = Crops(0, byteArray, name, quality.toInt(), selectedUnit,
            price.toDouble(), "Available", userId, userName, userPhone, time, userLocation
        )
        lifecycleScope.launch(Dispatchers.IO) {
            mCropViewModel.addCrop(newCrop)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@addCrop, "Crop Added", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private suspend fun compressAndConvertUriToByteArray(uri: Uri): ByteArray? = withContext(Dispatchers.IO) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream) // Compress to reduce size
        byteArrayOutputStream.toByteArray()
    }
}