package com.example.doform.DataBase


import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepository(private val appDAO: AppDAO) {

    suspend fun addUser(user: User){
        appDAO.insertUser(user)
    }

    suspend fun updateUser(user : User){
        appDAO.updateUser(user)
    }

    suspend fun getUser(email: String, password: String, userType: String): User? {
        return withContext(Dispatchers.IO) {
            appDAO.getUser(email, password, userType)
        }
    }

    suspend fun getUserById(id: Int): LiveData<User> {
        return appDAO.getUserById(id)
    }

}