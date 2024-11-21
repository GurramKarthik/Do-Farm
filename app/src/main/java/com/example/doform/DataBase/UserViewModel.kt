package com.example.doform.DataBase



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val appDAO = DofarmDatabase.getDatabase(application).appDAO()
        repository = UserRepository(appDAO)
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    suspend fun getUser(email: String, password: String, userType: String): User? {
        return repository.getUser(email, password, userType)
    }

    suspend fun getUserById(id: Int): LiveData<User> {
        return repository.getUserById(id)
    }

}