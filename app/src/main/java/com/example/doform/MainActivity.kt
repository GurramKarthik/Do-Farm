package com.example.doform

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.example.doform.Screens.Login_signUp
import com.example.doform.Screens.NewCrops
import com.example.doform.Screens.allCrops
import com.example.doform.Screens.editProfile
import com.example.doform.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        var UID = sharedPreferences.getString("userUID", null);
        binding.profile.setOnClickListener {
            if(UID == null){ Toast.makeText(this, "PLease Login", Toast.LENGTH_SHORT).show() }
            else {
                var intent = Intent(this, editProfile::class.java)
                startActivity(intent)
            }
            Toast.makeText(this, "${UID?: "No User" }", Toast.LENGTH_SHORT).show()
        }
        // Login And Sign Up And Sign Out
        val spinnerItems = arrayOf("")
        val customAdapter = object : ArrayAdapter<String>(this, R.layout.login_spinner, spinnerItems) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createViewFromResource(position, convertView, parent)
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return createViewFromResource(position, convertView, parent)
            }
            private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.login_spinner, parent, false)
                val button = view.findViewById<Button>(R.id.loginBtn)
                if(UID.isNullOrBlank()) {
                    button.text = "Login/SignUp ➡️"
                    button.setOnClickListener {
                        var intent = Intent(this@MainActivity, Login_signUp::class.java)
                        startActivity(intent);
                        finish()
                    }
                }else{
                    button.text = "⬅️ Sign Out "
                    button.setOnClickListener {
                        // User Is Logged In. Sign out.
                        var editor = sharedPreferences.edit()
                        editor.clear().commit()
                        Toast.makeText(this@MainActivity, "${UID}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        finish()
                    }
                }
                return view
            }
        }
        binding.loginSpinner.adapter = customAdapter;
        binding.spinnerTriggerBtn.setOnClickListener { binding.loginSpinner.performClick(); }

        binding.myCrops.setOnClickListener {
            if(UID == null){
                Toast.makeText(this, "PLease Login", Toast.LENGTH_SHORT).show()
            }else {
                var intent = Intent(this, allCrops::class.java)
                startActivity(intent)
//              Toast.makeText(this, "Open My Plants Activity", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

