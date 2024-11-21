package com.example.doform.Screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.doform.DataBase.User
import com.example.doform.DataBase.UserViewModel
import com.example.doform.MainActivity
import com.example.doform.R
import kotlinx.coroutines.launch

class editProfile : AppCompatActivity() {


    lateinit var mUserViewModel: UserViewModel
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var editName : EditText
    lateinit var editEmail : EditText
    lateinit var editPhone : EditText
    lateinit var editPassword : EditText
    lateinit var updateProfileBtn : Button
    lateinit var editLocation : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        mUserViewModel= ViewModelProvider(this).get(UserViewModel::class.java)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        editPassword = findViewById(R.id.editPassword)
        editLocation = findViewById(R.id.editLocation)
        updateProfileBtn = findViewById(R.id.updateProfileBtn)


        var UID = sharedPreferences.getString("userUID", null);
        var userId = UID?.toInt()!!
        var userType = sharedPreferences.getString("userType", null);


        lifecycleScope.launch {
            // Call the suspend function
            mUserViewModel.getUserById(userId).observe(this@editProfile, Observer { user ->
                if (user != null) {
                    // Update UI with the fetched user data
                    editName.setText(user.fullName)
                    editEmail.setText(user.email)
                    editPhone.setText(user.phone)
                    editPassword.setText(user.password)
                    editLocation .setText(user.location)
                }
            })
        }


        updateProfileBtn.setOnClickListener {

            var name = editName.text.toString()
            var phone = editPhone.text.toString()
            var email = editEmail.text.toString()
            var password = editPassword.text.toString()
            var location = editLocation.text.toString()

            if(validate(name, phone, email, password, location)) {
                var user = User( userId, name, phone,email,password, userType.toString(), location )
                mUserViewModel.updateUser(user)
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private  fun validate( name: String, phone: String,email: String,password: String, location:String ): Boolean {
        var isValid = true
        if (name.isBlank()) {
            editName.error = "Name cannot be empty"
            isValid = false
        }
        if (phone.isBlank()) {
            editPhone.error = "Phone number cannot be empty"
            isValid = false
        }
        if (email.isBlank()) {
            editEmail.error = "Email cannot be empty"
            isValid = false
        }
        if (password.isBlank()) {
            editPassword.error = "Password cannot be empty"
            isValid = false
        }
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            editPhone.error = "Invalid phone number. It should be 10 digits."
            isValid = false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.error = "Invalid email address"
            isValid = false
        }


        if (password.length < 6) {
            editPassword.error = "Password should be at least 6 characters long"
            isValid = false
        }

        if (location.isBlank()){
            editPassword.error = "Location cannot be empty"
            isValid = false

        }

        return isValid
    }

}