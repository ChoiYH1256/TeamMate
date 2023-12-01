package com.example.teammate

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Calendar
import android.Manifest
import android.widget.EditText

class PostCreateActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    //이미지 버튼 요청
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_create)

        textView = findViewById<TextView>(R.id.tv_date_picker)
        textView.setOnClickListener {
            DatePickerUtil.showDatePicker(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                textView.text = "${year}-${month + 1}-${dayOfMonth}"
            })
        }



    }

    //이미지 등록 버튼
    fun onAddImageButton(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            openGallery()
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri = data.data ?: return
            val imageView: ImageView = findViewById(R.id.iv_image)
            imageView.setImageURI(selectedImage)
        }
    }

    //뒤로 가기버튼
    fun onPostCreateBackButtonClick(view: View?){
        finish()
    }

    //글 작성 버튼 클릭 (유효하면 정보 저장 아니면 저장X)
    fun onSubmitButtonClick(view: View) {
        if (isInputValid()) {
            saveData()
        } else {
            Toast.makeText(this, "글 정보가 유효하지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    // 입력 데이터의 유효성을 검사하는 메서드
    private fun isInputValid(): Boolean {
        val titleEditText: EditText = findViewById(R.id.et_postcreatetitle)
        val contentEditText: EditText = findViewById(R.id.et_content)

        // 제목과 내용 필드가 비어있지 않은지 확인
        if (titleEditText.text.toString().trim().isEmpty() ||
            contentEditText.text.toString().trim().isEmpty()) {
            return false
        }

        // 여기에 다른 필드에 대한 유효성 검사 로직을 추가할 수 있습니다.

        return true
    }

    // 데이터를 저장하는 메서드
    private fun saveData() {
        // 여기에 데이터 저장 로직을 구현합니다.
        // 예: 파이어베이스 또는 로컬 데이터베이스에 저장

        Toast.makeText(this, "정보가 저장되었습니다.", Toast.LENGTH_LONG).show()
    }

}