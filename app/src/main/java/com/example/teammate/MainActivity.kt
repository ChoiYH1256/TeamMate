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

    fun onMainToAlarmButtonClick(view: View?){
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    fun onMainToMyPageButtonClick(view: View?){
        val intent = Intent(this, MyPageActivity::class.java)
        startActivity(intent)
    }

    fun onMainToPostCreateButtonClick(view: View?){
        val intent = Intent(this, PostCreateActivity::class.java)
        startActivity(intent)
    }

    fun onMainToChatButtonClick(view: View?){
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }

    fun onMainToPostButtonClick(view: View?){
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }

    fun onMainToHomeButtonClick(view: View?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun onMainToTalentButtonClick(view: View?){
        val intent = Intent(this, TalentActivity::class.java)
        startActivity(intent)
    }

}