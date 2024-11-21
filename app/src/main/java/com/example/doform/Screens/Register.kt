package com.example.doform.Screens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.doform.DataBase.User
import com.example.doform.DataBase.UserViewModel
import com.example.doform.R
import com.example.doform.dataClass.FarmerClass
import com.example.doform.dataClass.LoginRegister
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase


class Register : Fragment() {

    //data base
    /*
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var dbref: DatabaseReference
    */
    lateinit var mUserViewModel: UserViewModel



    lateinit var flag : LoginRegister
    lateinit var NameInput : EditText
    lateinit var phoneInput : EditText
    lateinit var emailInput : EditText
    lateinit var passwordInput : EditText
    lateinit var locationInput : EditText
    lateinit var registerbtn : Button
    lateinit var userType : Spinner
    private var selectedType: String = ""








    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout =layoutInflater.inflate(R.layout.fragment_register, container,false)
        flag = activity as LoginRegister



        mUserViewModel= ViewModelProvider(this).get(UserViewModel::class.java)

        NameInput = layout.findViewById<EditText>(R.id.newUser)
        phoneInput = layout.findViewById(R.id.phoneNum)
        emailInput = layout.findViewById(R.id.Email)
        passwordInput = layout.findViewById(R.id.password)
        registerbtn = layout.findViewById(R.id.register)
        userType = layout.findViewById<Spinner>(R.id.userType)
        locationInput = layout.findViewById(R.id.location)

        layout.findViewById<TextView>(R.id.existLogin).setOnClickListener {
            flag.logRegFlag(true)
        }


        val unitsAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arrayOf("Client", "Farmer")
        )

        userType.adapter = unitsAdapter
        userType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedType = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        registerbtn.setOnClickListener {

            val name = NameInput.text.toString()
            val phone = phoneInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val location = locationInput.text.toString()

            if(validate(name, phone, email, password, location)){
                saveUserToRoom(name, phone, email, password, location)
//                saveUserData(name, phone, email, password)
            }


        }
        return layout
    }

    private  fun validate( name: String, phone: String,email: String,password: String, location:String ): Boolean {
        var isValid = true
        if (name.isBlank()) {
            NameInput.error = "Name cannot be empty"
            isValid = false
        }
        if (phone.isBlank()) {
            phoneInput.error = "Phone number cannot be empty"
            isValid = false
        }
        if (email.isBlank()) {
            emailInput.error = "Email cannot be empty"
            isValid = false
        }
        if (password.isBlank()) {
            passwordInput.error = "Password cannot be empty"
            isValid = false
        }
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            phoneInput.error = "Invalid phone number. It should be 10 digits."
            isValid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Invalid email address"
            isValid = false
        }


        if (password.length < 6) {
            passwordInput.error = "Password should be at least 6 characters long"
            isValid = false
        }

        if (location.isBlank()) {
            passwordInput.error = "Location cannot be empty"
            isValid = false
        }

        return isValid
    }

    private fun showToast(s: String) {
        Toast.makeText(requireContext(),s, Toast.LENGTH_SHORT).show()
    }


    private fun saveUserToRoom(name: String, phone: String, email: String, password: String, location: String) {
        var newUser = User(0, name, phone, email, password, selectedType, location )
        mUserViewModel.addUser(newUser)
        showToast("Account Created Successfully")
    }





}