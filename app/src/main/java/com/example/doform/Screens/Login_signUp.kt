package com.example.doform.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.doform.R
import com.example.doform.dataClass.LoginRegister

class Login_signUp : AppCompatActivity() , LoginRegister {
    lateinit var fragmentManger : FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up)

        var login = Login()
        getFragment(login)

    }

    fun getFragment(fragmnet: Fragment) {
        fragmentManger = supportFragmentManager
        fragmentManger.beginTransaction().replace(R.id.regLog, fragmnet).commit()
    }

    override fun logRegFlag(flag: Boolean) {
        if(flag){
            var login = Login()
            getFragment(login)
        }else{
            var reg = Register()
            getFragment(reg)
        }
    }

}