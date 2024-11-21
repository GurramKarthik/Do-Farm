package com.example.doform.DataBase

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "crops_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Crops (
        @PrimaryKey(autoGenerate = true)
        val cropId: Int = 0,
        val cropImg: ByteArray?,
        val cropName: String,
        val quantity: Int,
        val unit : String,
        val price: Double,
        val status: String,
        val userOwnerId: Int,
        val oppositeName : String,
        val oppositePhone : String,
        val createdAt: Long = System.currentTimeMillis(),
        val location: String
)

