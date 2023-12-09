package com.example.teammate

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    // 현재 로그인된 사용자의 정보를 관리하는 객체 (예: FirebaseUser)


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
            // uid 가져옴
            val uid = getSavedUid()
            // 입력된 데이터를 가져옴

            // 데이터베이스에 업데이트하는 로직 (예: Firebase)
            // currentUser.updateProfile(...) 또는 다른 방법을 사용하여 업데이트
            if (uid != null)
            {
                val name = findViewById<EditText>(R.id.et_name).text.toString().trim()
                val phoneNumber = findViewById<EditText>(R.id.et_phonenumber).text.toString().trim()
                val email = findViewById<EditText>(R.id.et_id).text.toString().trim()
                val password = findViewById<EditText>(R.id.et_pw).text.toString().trim()
                val university = findViewById<EditText>(R.id.et_university).text.toString().trim()
                val major = findViewById<EditText>(R.id.et_major).text.toString().trim()
                val birthDate = findViewById<TextView>(R.id.tv_birth_date).text.toString().trim()
                val experience = findViewById<TextView>(R.id.et_experience).text.toString().trim()
                val portfolio = findViewById<TextView>(R.id.et_portfolio).text.toString().trim()
                val grade = findViewById<TextView>(R.id.et_grade).text.toString().trim()
                val area = findViewById<TextView>(R.id.et_area).text.toString().trim()
                val userProfile = UserProfile(
                    uid = uid,
                    name = name,
                    birth = birthDate,
                    phoneNumber = phoneNumber,
                    university = university,
                    experience = experience,
                    major = major,
                    grade = grade,
                    region = area
                    // 다른 필요한 필드들 추가
                )
                RetrofitClient.authService.editProfile(userProfile).enqueue(object : Callback<GenericResponse> {
                    override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ProfileActivity, "프로필 업데이트 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ProfileActivity, "업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                        Toast.makeText(this@ProfileActivity, "업데이트 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            }else{
                Toast.makeText(this, "현재 사용자 정보가 유효하지 않습니다", Toast.LENGTH_SHORT).show()
            }


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
        val major = findViewById<EditText>(R.id.et_major).text.toString().trim()
        val grade = findViewById<TextView>(R.id.et_grade).text.toString().trim()
        val area = findViewById<TextView>(R.id.et_area).text.toString().trim()

        return name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() &&
                password.isNotEmpty() && university.isNotEmpty() && birthDate.isNotEmpty()
                && major.isNotEmpty() && grade.isNotEmpty() && area.isNotEmpty()
    }

   //uid불러오기
    private fun getSavedUid(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", null) // uid 값 불러오기, 없으면 null 반환
    }
}