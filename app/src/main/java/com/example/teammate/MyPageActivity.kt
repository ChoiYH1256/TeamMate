package com.example.teammate

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val etBirth = findViewById<EditText>(R.id.et_birth)
        etBirth.setOnClickListener {
            DatePickerUtil.showDatePicker(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // 날짜 형식을 원하는 대로 조정하세요. 여기서는 YYYY-MM-DD 형식을 사용합니다.
                val selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)
                etBirth.setText(selectedDate)
            })
        }
    }
}