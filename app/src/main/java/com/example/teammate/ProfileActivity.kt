package com.example.teammate

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    // 현재 로그인된 사용자의 정보를 관리하는 객체 (예: FirebaseUser)
    // private val currentUser = ...


    //생년월일 textview
    private lateinit var tvBirthDate: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //생년월일 입력 필드 생성
        tvBirthDate = findViewById(R.id.tv_birth_date)
        tvBirthDate.setOnClickListener {
            DatePickerUtil.showDatePicker(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // 선택된 날짜를 TextView에 설정합니다.
                val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
                tvBirthDate.text = selectedDate
            })
        }

    }

    //취소 버튼
    fun onProfileCancleButtonClick(view: View?){
        finish()
    }

    //수정버튼
    fun onProfileOkButtonClick(view: View) {
        if (validateInputs()) {
            // 입력된 데이터를 가져옴
            val name = findViewById<EditText>(R.id.et_name).text.toString().trim()
            val phoneNumber = findViewById<EditText>(R.id.et_phonenumber).text.toString().trim()
            val email = findViewById<EditText>(R.id.et_id).text.toString().trim()
            val password = findViewById<EditText>(R.id.et_pw).text.toString().trim()
            val university = findViewById<EditText>(R.id.et_university).text.toString().trim()
            val birthDate = findViewById<TextView>(R.id.tv_birth_date).text.toString().trim()
            val experience = findViewById<TextView>(R.id.et_experience).text.toString().trim()
            val portfolio = findViewById<TextView>(R.id.et_portfolio).text.toString().trim()

            // 데이터베이스에 업데이트하는 로직 (예: Firebase)
            // currentUser.updateProfile(...) 또는 다른 방법을 사용하여 업데이트
        } else {
            // 유효성 검사 실패
            Toast.makeText(this, "모든 필수 정보를 입력해야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        // 필수 입력 필드 확인
        val name = findViewById<EditText>(R.id.et_name).text.toString().trim()
        val phoneNumber = findViewById<EditText>(R.id.et_phonenumber).text.toString().trim()
        val email = findViewById<EditText>(R.id.et_id).text.toString().trim()
        val password = findViewById<EditText>(R.id.et_pw).text.toString().trim()
        val university = findViewById<EditText>(R.id.et_university).text.toString().trim()
        val birthDate = findViewById<TextView>(R.id.tv_birth_date).text.toString().trim()

        return name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() &&
                password.isNotEmpty() && university.isNotEmpty() && birthDate.isNotEmpty()
    }

    // 이메일과 전화번호 인증 로직에 따른 추가 메서드 구현 필요
}