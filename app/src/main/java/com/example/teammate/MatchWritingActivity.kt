package com.example.teammate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchWritingActivity : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var postUid: String
    private lateinit var selfIntroductionEditText: EditText
    //private lateinit var resumeEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_writing)


        postId = intent.getStringExtra("POST_ID") ?: ""
        postUid = intent.getStringExtra("POST_UID") ?: ""
        selfIntroductionEditText = findViewById(R.id.et_introduction)
        //resumeEditText = findViewById(R.id.editTextResume)
        // 툴바를 찾아서 설정
        val toolbar: Toolbar = findViewById(R.id.btn_cancel)
        setSupportActionBar(toolbar)

        // 뒤로 가기 버튼(navigation icon) 클릭 이벤트 처리
        toolbar.setNavigationOnClickListener {
            // 현재 액티비티 종료 (뒤로 가기)
            onBackPressed()
        }
    }
    fun onApplicantSubmitButtonClick(view: View){
        val applicantUid = getSavedUid() ?: return
        val selfIntroduction = selfIntroductionEditText.text.toString()
        val resume = "임시 이력서 내용" // 임시 이력서 내용

        if(selfIntroduction.isEmpty()){
            Toast.makeText(this, "자기소개를 작성해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val application = ApplicationCreate(postId, postUid, applicantUid, selfIntroduction, resume)
        RetrofitClient.applicationService.createApplication(application).enqueue(object : Callback<GenericResponse>{
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                val statusCode = response.code() // HTTP 상태 코드
                Log.d("Response", "서버응답: ${response.body()}")
                Log.d("Response", "상태코드: $statusCode")
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "지원서가 성공적으로 제출되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // 현재 액티비티 종료
                } else {
                    Toast.makeText(applicationContext, "지원서 제출에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getSavedUid(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", null) // uid 값 불러오기, 없으면 null 반환
    }
}