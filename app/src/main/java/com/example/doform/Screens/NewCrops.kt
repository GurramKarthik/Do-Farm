package com.example.doform.Screens

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doform.Recycler.CropsRecycler
import com.example.doform.dataClass.Crop
import com.example.doform.databinding.ActivityNewCropsBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.file.attribute.AclEntry.Builder
import java.util.logging.Handler


class NewCrops : AppCompatActivity() {
    lateinit var response :GenerateContentResponse
    lateinit var binding: ActivityNewCropsBinding
    lateinit var recyclerView: RecyclerView
//    private var Recommendedcrops: List<responseData>? = null
    var apiRequest: Boolean = false
    lateinit var prompt :String

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCropsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.CropsRecyclerView.layoutManager = GridLayoutManager(this, 2);

        val seasonsAdopter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf(
                "Select",
                "Summer",
                "Rainy",
                "Winter",
                "Early Rainy",
                "Year-Round(Perennial Crops) "
            )

        )
        binding.seasonsSpinner.adapter = seasonsAdopter

        val soilAdopter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf(
                "Select",
                "Alluvial Soil",
                "Black Soil (Regular Soil)",
                "Red Soil",
                "Laterite Soil",
                "Loamy Soil",
                "Sandy Soil",
                "Clay Soil",
                "Peaty Soil",
                "Saline Soil",
                "Desert Soil"
            )
        )
        binding.SoilSpinner.adapter = soilAdopter

        val waterAdopter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf("Select", "High", "Medium", "low")
        )
        binding.waterSpinner.adapter = waterAdopter

        val cropsAdaptor = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf("Select", "Food Grains", "Cash Crops", "Vegetables", "Fruits")
        )
        binding.cropSpinner.adapter = cropsAdaptor



        binding.generateCropsBtn.setOnClickListener {
            var season = getItem(binding.seasonsSpinner).toString()
            var soil = getItem(binding.SoilSpinner).toString()
            var water = getItem(binding.waterSpinner).toString()
            var cropType = getItem(binding.cropSpinner).toString()

            val cropsList : List<Crop>?

            if (!apiRequest) {
                apiRequest =true

                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Loading, please wait...")
                progressDialog.setCancelable(false) // Prevent dismissal on back button press

                // Show the dialog
                progressDialog.show()

                // Simulate a background task (e.g., network request)


               cropsList= generateCrops(season, soil, water, cropType);
                binding.CropsRecyclerView.layoutManager = GridLayoutManager(this, 2)
                binding.CropsRecyclerView.adapter = CropsRecycler(this, cropsList)
                apiRequest = false

                progressDialog.dismiss()




        }
    }
}

    private fun getItem(spinner: Spinner): String {
        var selectedItem: String = "";
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        return selectedItem;
    }

    private fun generateCrops(season:String, soil:String, water:String, cropType:String): List<Crop>?{
         prompt = """
                    Recommend a list of crops at-mostssss 5 based on the following requirements: Land type: black soil, Season: Summer, Water availability: medium, Crop type: fruits. 
                    Please provide the output in JSON format so that I can use it and do not include "JSON" key word or any other, where each crop object contains the following details:
                    Always give me the bellow format,
                    [
                        {
                            "Name": CropName,
                            "Description": A brief description of the crop, 
                            "ApproximateNumberOfDays": The approximate number of days required for the crop to grow and be ready for harvest,
                            "Pesticides": { "organic" : [ organic Pesticides list],   "Inorganic": [ inorganic pesticide list] }
                             "IrrigationType": The type of irrigation required for that crop
                        }
                    ]
                    """.trimIndent();


            val generativeModel = GenerativeModel(
//              modelName = "gemini-pro",
                modelName = "gemini-1.0-pro",
//              modelName = "gemini-1.5-pro", // try if above model does not work to generate json like objects
                apiKey = "Put you API key here"
            )


            var cropsList: List<Crop>? = null;


// Inside your Activity or Fragment
                lifecycleScope.launch {
                    try {
                        val response = generativeModel.generateContent(prompt)
                        Log.d("Converting to JSON", "generateCrops: ")
                        try {
                            // Uncomment and use Gson if needed
                             val gson = Gson()
                             val listType = object : TypeToken<List<Crop>>() {}.type
                             cropsList = gson.fromJson(response.text, listType)

                            binding.resTxt.text = cropType.toString()

                            withContext(Dispatchers.Main) {
                                binding.CropsRecyclerView.adapter = CropsRecycler(this@NewCrops, cropsList)
                            }

                          //  binding.resTxt.text = response.text.toString().trimIndent()
                            Log.d("Done to JSON", "${cropsList} ")
                        } catch (err: Exception) {
                            Log.e("Error in converting", "${err}")
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@NewCrops, "Connect to the Internet", Toast.LENGTH_SHORT).show()
                    }
                }

        return cropsList;


    }

}


