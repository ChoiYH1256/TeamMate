package com.example.teammate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val emailEditText = findViewById<EditText>(R.id.et_id)
        val passwordEditText = findViewById<EditText>(R.id.et_pw)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        RetrofitClient.authService.loginUser(UserLogin(email, password))
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    Log.d("Response", "서버 응답: ${response.body()}")
                    if (response.isSuccessful) {
                        // 로그인 성공 처리
                        val uid = response.body()?.uid
                        saveUid(uid)
                        navigateToNextActivity()

                        val message = response.body()?.message ?: "로그인 성공"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    } else {
                        // 에러 처리
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                    Log.e("LoginError", "로그인 요청 실패: ", t) //디버깅용
                    Toast.makeText(applicationContext, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveUid(uid: String?) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uid", uid)
        editor.apply() // 또는 commit() 사용
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }

}