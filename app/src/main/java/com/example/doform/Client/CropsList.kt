package com.example.doform.Client

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.doform.R
import com.example.doform.Screens.cartFrag
import com.example.doform.Screens.myPlantsFrag1
import com.google.android.material.bottomnavigation.BottomNavigationView

class CropsList : AppCompatActivity() {

    lateinit var bottombar: BottomNavigationView
    lateinit var fragmentManger : FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crops_list)
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
                    fragmentManger.beginTransaction().replace(R.id.plantsActFrag, plantsListFrag()).commit()
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