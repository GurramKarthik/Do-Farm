package com.example.doform

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.doform.Client.Home

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val userType = sharedPreferences.getString("userType", "Client") // Default value "Client"

        // Delay for splash screen duration (e.g., 2 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate based on userType
            if (userType == "Client") {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish() // Close SplashActivity
        }, 2000) // 2-second delay


    }
}