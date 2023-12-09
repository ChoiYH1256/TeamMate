package com.example.teammate

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)


        fetchAndDisplayUserProfile()
        setupProfileEditButton()
    }

    private fun fetchAndDisplayUserProfile() {
        val uid = getSavedUid()
        if (uid != null) {
            RetrofitClient.authService.getProfile(uid).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        val userProfile = response.body()
                        displayUserProfile(userProfile)
                    } else {
                        // 서버 오류 메시지 표시
                        val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류 발생"
                        Toast.makeText(applicationContext, "오류: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    // 네트워크 오류 메시지 표시
                    Toast.makeText(applicationContext, "네트워크 오류: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun setupProfileEditButton() {
        val btnEditProfile = findViewById<Button>(R.id.btn_phone_auth2)
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun displayUserProfile(userProfile: UserProfile?) {
        userProfile?.let {
            findViewById<TextView>(R.id.tv_name).text = it.name
        }
    }
    private fun getSavedUid(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", null)
    }
}