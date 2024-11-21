package com.example.doform.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.doform.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class allCrops : AppCompatActivity() {
    lateinit var bottombar: BottomNavigationView
    lateinit var fragmentManger : FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_crops)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fragmentManger = supportFragmentManager
        //bottom bar
        bottomBar(this)
    }

    @SuppressLint("SuspiciousIndentation")
    fun bottomBar(context : Context){
        bottombar = findViewById(R.id.bottomBar)
        bottombar.selectedItemId = R.id.myplants
        bottombar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.myplants -> {
                    //calling fragments
                    fragmentManger.beginTransaction().replace(R.id.plantsActFrag, myPlantsFrag1()).commit()
                    true
                }
                R.id.cart -> {
                    fragmentManger.beginTransaction().replace(R.id.plantsActFrag, cartFrag()).commit()
                    true
                }
                else -> {
                    true
                }
            }

        }
    }
}