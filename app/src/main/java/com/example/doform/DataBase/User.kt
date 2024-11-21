package com.example.doform.DataBase


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int,
    val fullName: String,
    val phone: String,
    val email: String,
    val password: String,
    val userType:String,
    val location :String
)
