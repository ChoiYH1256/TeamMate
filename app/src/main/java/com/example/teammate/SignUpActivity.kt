package com.example.teammate

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    // 인증 상태를 저장하는 변수 (실제 인증 로직에 따라 수정 필요)
    private var isEmailVerified = true //나중에 false로 바꿔주기 (디버깅용)
    private var isPhoneNumberVerified = true
    private var verificationId: String? = null


    //파일 첨부 textview
    private lateinit var tvExperience: TextView
    private lateinit var tvPortfolio: TextView
    //생년월일 textview
    private lateinit var tvBirthDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.btn_phone_auth).setOnClickListener {
            val phoneNumber = findViewById<EditText>(R.id.et_phonenumber).text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                startPhoneNumberVerification(phoneNumber)
            } else {
                Toast.makeText(this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        //생년월일 입력 필드 생성
        tvBirthDate = findViewById(R.id.tv_birth_date)
        tvBirthDate.setOnClickListener {
            DatePickerUtil.showDatePicker(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // 선택된 날짜를 TextView에 설정합니다.
                val selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth)
                tvBirthDate.text = selectedDate
            })
        }
        findViewById<Button>(R.id.btn_verify_otp).setOnClickListener {
            val otp = findViewById<EditText>(R.id.et_otp).text.toString()
            if(otp.isNotEmpty() && verificationId != null) {
                verifyPhoneNumberWithCode(verificationId!!, otp)
            } else {
                Toast.makeText(this, "인증번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }


        //파일 첨부
        tvExperience = findViewById(R.id.et_experience)
        tvPortfolio = findViewById(R.id.tv_portfolio2)
        findViewById<Button>(R.id.btn_experience_submit).setOnClickListener {
            openFilePicker(EXPERIENCE_FILE_PICK)
        }

        findViewById<Button>(R.id.btn_portfolio_submit).setOnClickListener {
            openFilePicker(PORTFOLIO_FILE_PICK)
        }

    }

    // 전화번호 인증 시작 함수
    private fun startPhoneNumberVerification(phoneNumber: String) {
        val formattedPhoneNumber = "+82" + phoneNumber.substring(1)
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(formattedPhoneNumber) // 변환된 전화번호
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // 전화번호 인증 콜백
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // 인증이 자동으로 완료되었을 때 호출됩니다.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // 인증 실패 시 호출됩니다.
            Toast.makeText(this@SignUpActivity, "인증 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("ddddd","${e.message}")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // OTP가 전송되었을 때 호출됩니다.
            Toast.makeText(this@SignUpActivity,"인증번호를 보냈습니다!", Toast.LENGTH_SHORT).show()
            this@SignUpActivity.verificationId = verificationId
        }
    }

    // 전화번호 인증으로 로그인
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 인증 성공
                    isPhoneNumberVerified = true
                    Toast.makeText(this@SignUpActivity, "전화번호 인증 성공", Toast.LENGTH_SHORT).show()
                } else {
                    // 인증 실패
                    Toast.makeText(this@SignUpActivity, "전화번호 인증 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // OTP 인증 함수
    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    //파일 첨부
    private fun openFilePicker(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // 모든 파일 타입을 허용합니다. 특정 파일 타입만 허용하려면 변경하세요.
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val fileName = uri?.let { getFileName(it) }

            when (requestCode) {
                EXPERIENCE_FILE_PICK -> tvExperience.text = fileName
                PORTFOLIO_FILE_PICK -> tvPortfolio.text = fileName
            }
        }
    }



    private fun getFileName(uri: Uri): String {
        var name = ""
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }

    companion object {
        private const val EXPERIENCE_FILE_PICK = 1
        private const val PORTFOLIO_FILE_PICK = 2
    }

    fun onSignUpCancleButtonClick(view: View?){
        finish()
    }


    // 회원가입 버튼 클릭 (회원가입로직)
    // 회원가입 로직은 성공 (실제 계정 생성확인)
    // region, major, grade 가 UI에 포함돼야함
    fun onSignUpButtonClick(view: View) {
        if (validateInputs() && isEmailVerified && isPhoneNumberVerified) {
            val region = findViewById<EditText>(R.id.et_name).text.toString()
            val email = findViewById<EditText>(R.id.et_id).text.toString()
            val password = findViewById<EditText>(R.id.et_pw).text.toString()
            val major = findViewById<EditText>(R.id.et_university).text.toString()
            val grade = findViewById<EditText>(R.id.et_phonenumber).text.toString()



            RetrofitClient.registerService.signupUser(UserSignup(email,password,major,grade,region))
                .enqueue(object: Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>){
                        val statusCode = response.code() // HTTP 상태 코드
                        Log.d("Response", "서버응답: ${response.body()}")
                        Log.d("Response", "오류: $statusCode")
                        if(response.isSuccessful){
                            val message = response.body()?.message ?: "회원가입 성공"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        } else {
                            // 에러 처리
                            Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                        Log.e("LoginError", "로그인 요청 실패: ", t) //디버깅용
                        Toast.makeText(applicationContext, "네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            // 회원가입 처리
            // Firebase나 다른 백엔드 서비스를 사용하여 사용자 정보 저장

        } else {
            // 유효성 검사 실패 또는 인증이 완료되지 않음
            Toast.makeText(this, "모든 필수 정보를 입력하고 인증을 완료해야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 입력을 받고 입력이 비어있지 않은지 확인
    private fun validateInputs(): Boolean {
        val username = findViewById<EditText>(R.id.et_name).text.toString().trim()
        val phoneNumber = findViewById<EditText>(R.id.et_phonenumber).text.toString().trim()
        val email = findViewById<EditText>(R.id.et_id).text.toString().trim()
        val password = findViewById<EditText>(R.id.et_pw).text.toString().trim()
        val university = findViewById<EditText>(R.id.et_university).text.toString().trim()
        val birthDate = findViewById<TextView>(R.id.tv_birth_date).text.toString().trim()

        // 필수 필드가 비어있지 않은지 확인
        return username.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() &&
                password.isNotEmpty() && university.isNotEmpty() && birthDate.isNotEmpty()
    }

    // 여기에 이메일과 전화번호 인증 관련 코드 추가 (예: 인증 버튼 클릭 리스너)

}