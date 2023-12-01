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
    fun onPostCreateBackButtonClick(view: View?){
        finish()
    }
}