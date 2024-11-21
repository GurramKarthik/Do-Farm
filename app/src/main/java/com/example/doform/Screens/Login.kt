package com.example.doform.Screens

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.doform.Client.Home
import com.example.doform.DataBase.UserViewModel
import com.example.doform.MainActivity
import com.example.doform.R
import com.example.doform.dataClass.LoginRegister
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class Login : Fragment() {

    //data base
    /*
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var dbref: DatabaseReference
    */
    lateinit var mUserViewModel: UserViewModel


    lateinit var flag : LoginRegister
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var emailInput : EditText
    lateinit var passwordInput : EditText

    lateinit var userType :Spinner

    private var selectedType: String = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var layout = inflater.inflate(R.layout.fragment_login, container, false)
        flag = activity as LoginRegister


        mUserViewModel= ViewModelProvider(this).get(UserViewModel::class.java)


        sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)


        layout.findViewById<TextView>(R.id.newSignIn).setOnClickListener {
            flag.logRegFlag(false)
        }




        emailInput = layout.findViewById<EditText>(R.id.loginEmail)
        passwordInput = layout.findViewById<EditText>(R.id.LoginPassword)


        userType = layout.findViewById<Spinner>(R.id.userType)

        val loginBtn = layout.findViewById<Button>(R.id.loginbtn)

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


        loginBtn.setOnClickListener {
            var email = emailInput.text.toString()
            var password = passwordInput.text.toString()
            if(validate(email, password)){
                loginFromRoom(email, password)
            }

        }

        return layout
    }

    private fun validate(email: String,password: String ): Boolean {
        var isValid = true
        if (email.isBlank()) {
            emailInput.error = "Email cannot be empty"
            isValid = false
        }
        if (password.isBlank()) {
            passwordInput.error = "Password cannot be empty"
            isValid = false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Invalid email address"
            isValid = false
        }
        return isValid
    }

    private fun loginFromRoom(email: String, password: String) {
        val progressBar = ProgressDialog(requireActivity())
        progressBar.setTitle("Logging In....")
        progressBar.setMessage("Please wait while we login for you....")
        progressBar.show()

        lifecycleScope.launch {
            try {
                val user = mUserViewModel.getUser(email, password, selectedType)
                if (user != null) {
                    saveUser(user.userId.toString(), user.userType, user.fullName, user.phone, user.location)
                    if(user.userType == "Client"){
                        val intent = Intent(requireActivity(), Home::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                    }
                } else {
                    Toast.makeText(requireActivity(), "Invalid Credentials!", Toast.LENGTH_SHORT).show()
                }
                progressBar.dismiss()

            } catch (e: Exception) {
                progressBar.dismiss()  // Ensure to dismiss the progress bar even if there's an error
                Toast.makeText(requireActivity(), "Error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun saveUser(userId: String, userType: String, userName:String, userPhone:String, userLocation:String){
        var intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        val editor = sharedPreferences.edit()
        editor.putString("userUID",userId)
        editor.putString("userType",userType)
        editor.putString("userName",userName)
        editor.putString("userPhone",userPhone)
        editor.putString("userLocation",userLocation)
        editor.apply()
    }








}