package com.example.teammate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    //로그인 화면에서 회원가입 화면으로 이동
    fun onSignInToSignUpButtonClick(view: View?){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    //로그인 버튼 클릭시 로그인하는 로직 구현
    fun onSignInButtonClick(view: View?){
    }
}