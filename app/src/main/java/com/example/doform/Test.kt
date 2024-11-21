package com.example.doform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val req = findViewById<EditText>(R.id.promptTxt)
        val subBnt = findViewById<Button>(R.id.submitBtn)
        val restext = findViewById<TextView>(R.id.resTxt)

        subBnt.setOnClickListener {
            val prompt = req.text.toString()
            val generativeModel = GenerativeModel(
//              modelName = "gemini-pro",
                modelName = "gemini-1.0-pro",
//              modelName = "gemini-1.5-pro", // try if above model does not work to generate json like objects
                apiKey = "AIzaSyD7F5e-8dIBD_fJpro69995J2Om-T0bFG0"
            )
            runBlocking {
                val response = generativeModel.generateContent(prompt)
                restext.text = response.text
            }
        }
    }
}