package com.example.teammate

import DatePickerUtil
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import java.util.concurrent.TimeUnit


class SignUpActivity : AppCompatActivity() {

    // 인증 상태를 저장하는 변수 (실제 인증 로직에 따라 수정 필요)
    private var isEmailVerified = false //나중에 false로 바꿔주기 (디버깅용)
    private var isPhoneNumberVerified = false
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
        tvPortfolio = findViewById(R.id.et_portfolio)
        findViewById<Button>(R.id.btn_experience_submit).setOnClickListener {
            openFilePicker(EXPERIENCE_FILE_PICK)
        }

        findViewById<Button>(R.id.btn_portfolio_submit).setOnClickListener {
            openFilePicker(PORTFOLIO_FILE_PICK)
        }

    }

    fun onSignUpButtonClick(view: View) {
        if (validateInputs() && isEmailVerified && isPhoneNumberVerified) {
            val region = findViewById<EditText>(R.id.et_area).text.toString()
            val email = findViewById<EditText>(R.id.et_id).text.toString()
            val password = findViewById<EditText>(R.id.et_pw).text.toString()
            val major = findViewById<EditText>(R.id.et_major).text.toString()
            val grade = findViewById<EditText>(R.id.et_grade).text.toString()

            // 플러스 프로필 생성
            val name = findViewById<EditText>(R.id.et_name).text.toString()
            val birth = findViewById<TextView>(R.id.tv_birth_date).text.toString()
            val university = findViewById<EditText>(R.id.et_university).text.toString()
            val experience = findViewById<TextView>(R.id.et_experience).text.toString()
            val phoneNumber = findViewById<TextView>(R.id.et_phonenumber).text.toString()


            RetrofitClient.authService.signupUser(UserSignup(email,password,major,grade,region))
                .enqueue(object: Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>){
                        val statusCode = response.code() // HTTP 상태 코드
                        Log.d("Response", "서버응답: ${response.body()}")
                        Log.d("Response", "오류: $statusCode")
                        if(response.isSuccessful){
                            val uid = response.body()?.uid

                            createProfile(uid, name, birth, phoneNumber, university,experience, major, grade, region)

                            val message = response.body()?.message ?: "회원가입 성공"
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            finish()
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

    fun onIdAuthButtonClick(view: View) {
        if (isPhoneNumberVerified) {
            val email = findViewById<EditText>(R.id.et_id).text.toString()
            if(!(isValidDomain(email))){
                Toast.makeText(this, "대학생 이메일이 아닙니다.", Toast.LENGTH_LONG).show()
                return
            }
            if (email.isNotEmpty()) {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, "yourPassword") // 사용자 비밀번호 입력
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "인증 이메일 발송됨: $email", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else{
                            Toast.makeText(this, "이미 등록된 이메일 주소입니다.", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "이메일 주소를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(this, "전화번호 인증을 완료해야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //이메일 인증완료 버튼
    fun onOtpEmailButtonClick(view: View){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Firebase 사용자 정보 업데이트가 성공한 경우
                    isEmailVerified = user.isEmailVerified
                    if (isEmailVerified) {
                        // 이메일 인증이 완료된 경우
                        Toast.makeText(this, "이메일 인증이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        deleteUserAccount(user)
                    } else {
                        // 이메일 인증이 아직 완료되지 않은 경우
                        Toast.makeText(this, "이메일 인증이 아직 완료되지 않았습니다.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Firebase 사용자 정보 업데이트 실패
                    Toast.makeText(this, "사용자 정보 업데이트 실패: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
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

    fun onSignUpCancelButtonClick(view: View?){
        finish()
    }


    // 회원가입 버튼 클릭 (회원가입로직)
    // 회원가입 로직은 성공 (실제 계정 생성확인)
    // region, major, grade 가 UI에 포함돼야함


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

    //프로필 생성
    private fun createProfile(uid: String?, name: String, birth: String, phoneNumber: String, university: String, experience: String, major: String, grade: String, region: String) {
        if (uid != null) {
            val profileData = UserProfile(uid, name, birth, phoneNumber, university, experience, major, grade, region)
            RetrofitClient.authService.createProfile(profileData)
                .enqueue(object : Callback<GenericResponse> {
                    override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                        val statusCode = response.code() // HTTP 상태 코드
                        Log.d("ProfileResponse", "서버응답: ${response.body()}")
                        Log.d("ProfileResponse", "오류: $statusCode")
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "프로필 생성 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "프로필 생성 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "프로필 생성 중 네트워크 오류", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }


    //임시 전화번호 아이디 삭제
    private fun deleteUserAccount(user: FirebaseUser) {
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 계정 삭제 성공
                } else {
                    // 계정 삭제 실패
                }
            }
    }

    fun isValidDomain(email: String): Boolean {
// 이메일 주소에서 '@'를 기준으로 도메인 부분을 추출합니다.
        // 이메일 주소에서 '@'를 기준으로 도메인 부분을 추출합니다.
        val parts: List<String> = email.split("@")

        if (parts.size != 2) {
            // '@'가 하나가 아닌 경우 유효하지 않은 형식입니다.
            return false
        }

        // 도메인 부분을 추출합니다.
        val domain = parts[1]

        // 대학교 도메인 리스트를 만듭니다.

        // 대학교 도메인 리스트를 만듭니다.
        val validDomains = arrayOf(
            "gachon.ac.kr",
            "csj.ac.kr",
            "gangdong.ac.kr",
            "gyc.ac.kr",
            "kt.ac.kr",
            "gw.ac.kr",
            "gc.ac.kr",
            "namhae.ac.kr",
            "kit.ac.kr",
            "kyungmin.ac.kr",
            "kbu.ac.kr",
            "kbsc.ac.kr",
            "gpc.ac.kr",
            "kbc.ac.kr",
            "gs.ac.kr",
            "kwc.ac.kr",
            "kic.ac.kr",
            "kmcu.ac.kr",
            "kaywon.ac.kr",
            "kgrc.ac.kr",
            "kwangyang.ac.kr",
            "ghu.ac.kr",
            "gumi.ac.kr",
            "koje.ac.kr",
            "kookje.ac.kr",
            "kcn.ac.kr",
            "kunjang.ac.kr",
            "ccn.ac.kr",
            "kcs.ac.kr",
            "gimcheon.ac.kr",
            "kimpo.ac.kr",
            "gimhae.ac.kr",
            "nonghyup.ac.kr",
            "tk.ac.kr",
            "ttc.ac.kr",
            "tsu.ac.kr",
            "dfc.ac.kr",
            "dhc.ac.kr",
            "ddu.ac.kr",
            "daedong.ac.kr",
            "daelim.ac.kr",
            "daewon.ac.kr",
            "hit.ac.kr",
            "dkc.ac.kr",
            "dongnam.ac.kr",
            "tu.ac.kr",
            "dpc.ac.kr",
            "dsc.ac.kr",
            "dima.ac.kr",
            "dongac.ac.kr",
            "dongyang.ac.kr",
            "duc.ac.kr",
            "dist.ac.kr",
            "tw.ac.kr",
            "dit.ac.kr",
            "dongju.ac.kr",
            "doowon.ac.kr",
            "masan.ac.kr",
            "mjc.ac.kr",
            "mokpo-c.ac.kr",
            "mjc.ac.kr",
            "mokpo-c.ac.kr",
            "mkc.ac.kr",
            "baewha.ac.kr",
            "bsc.ac.kr",
            "bsks.ac.kr",
            "bist.ac.kr",
            "bwc.ac.kr",
            "busanarts.ac.kr",
            "bc.ac.kr",
            "shu.ac.kr",
            "syu.ac.kr",
            "sy.ac.kr",
            "sorabol.ac.kr",
            "seoyeong.ac.kr",
            "shjc.ac.kr",
            "snjc.ac.kr",
            "seoularts.ac.kr",
            "seoil.ac.kr",
            "seojeong.ac.kr",
            "sohae.ac.kr",
            "sunlin.ac.kr",
            "sdc.ac.kr",
            "sungsim.ac.kr",
            "saekyung.ac.kr",
            "songgok.ac.kr",
            "songwon.ac.kr",
            "songho.ac.kr",
            "sc.ac.kr",
            "ssc.ac.kr",
            "swc.ac.kr",
            "suncheon.ac.kr",
            "sewc.ac.kr",
            "shingu.ac.kr",
            "shinsung.ac.kr",
            "sau.ac.kr",
            "shc.ac.kr",
            "motor.ac.kr",
            "asc.ac.kr",
            "ansan.ac.kr",
            "yit.ac.kr",
            "yeonsung.ac.kr",
            "yonam.ac.kr",
            "yc.ac.kr",
            "yflc.ac.kr",
            "ync.ac.kr",
            "ycc.ac.kr",
            "yjc.ac.kr",
            "osan.ac.kr",
            "ysc.ac.kr",
            "wst.ac.kr",
            "wsi.ac.kr",
            "uc.ac.kr",
            "wat.ac.kr",
            "wkhc.ac.kr",
            "wonju.ac.kr",
            "yuhan.ac.kr",
            "induk.ac.kr",
            "jeiu.ac.kr",
            "icc.ac.kr",
            "itc.ac.kr",
            "jangan.ac.kr",
            "chunnam-c.ac.kr",
            "dorip.ac.kr",
            "jbsc.ac.kr",
            "jk.ac.kr",
            "jvision.ac.kr",
            "ctc.ac.kr",
            "jeju.ac.kr",
            "chu.ac.kr",
            "cnc.ac.kr",
            "cst.ac.kr",
            "jhc.ac.kr",
            "csc.ac.kr",
            "cmu.ac.kr",
            "yonam.ac.kr",
            "chungkang.academy",
            "scjc.ac.kr",
            "ch.ac.kr",
            "cyc.ac.kr",
            "cpu.ac.kr",
            "chsu.ac.kr",
            "ok.ac.kr",
            "pohang.ac.kr",
            "kg.ac.kr",
            "ktc.ac.kr",
            "af.ac.kr",
            "hanrw.ac.kr",
            "klc.ac.kr",
            "pro.ac.kr",
            "icpc.ac.kr",
            "krc.ac.kr",
            "kopo.ac.kr", // 추가 도메인
            "hsc.ac.kr", // 추가 도메인
            "hywoman.ac.kr", // 추가 도메인
            "hanyeong.ac.kr", // 추가 도메인
            "hj.ac.kr", // 추가 도메인
            "hu.ac.kr", // 추가 도메인
            "kaya.ac.kr", // 추가 도메인
            "ggu.ac.kr", // 추가 도메인
            "gwnu.ac.kr", // 추가 도메인
            "knu.ac.kr", // 추가 도메인
            "daejeon.ac.kr", // 추가 도메인
            "ajou.ac.kr"
        )

        // 대학교 도메인 리스트에 포함되어 있는지 확인합니다.

        // 대학교 도메인 리스트에 포함되어 있는지 확인합니다.
        for (validDomain in validDomains) {
            if (domain == validDomain) {
                return true
            }
        }

        // 대학교 도메인 리스트에 포함되지 않으면 유효하지 않은 도메인입니다.

        // 대학교 도메인 리스트에 포함되지 않으면 유효하지 않은 도메인입니다.
        return false

    }





}