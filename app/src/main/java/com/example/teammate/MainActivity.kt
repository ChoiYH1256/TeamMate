package com.example.teammate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onMainToSignUpButtonClick(view: View?){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun onMainToSignInButtonClick(view: View?){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun onMainToProfileButtonClick(view: View?){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

}