package com.example.doform.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class, Crops::class], version = 2, exportSchema = false)
abstract class DofarmDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
    companion object {
        @Volatile
        private var INSTANCE: DofarmDatabase? = null

        // Get the database instance
        fun getDatabase(context: Context): DofarmDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DofarmDatabase::class.java,
                    "dofarm5_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
