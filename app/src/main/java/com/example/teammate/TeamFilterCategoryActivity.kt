package com.example.teammate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
class TeamFilterCategoryActivity : AppCompatActivity() {

    private lateinit var majorSpinner: Spinner
    private lateinit var btnShowResults: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_filter_category)


        majorSpinner = findViewById(R.id.major_spinner)
        btnShowResults = findViewById(R.id.btn_cancel)


        // 전공 목록 예시 - 실제 사용 시 데이터베이스나 리소스에서 가져옵니다.
        val majors = arrayOf("소프트웨어", "전공2", "전공3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, majors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        majorSpinner.adapter = adapter

        btnShowResults.setOnClickListener {
            val selectedMajor = majorSpinner.selectedItem.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("selectedMajor", selectedMajor)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }



        val btnCancel = findViewById<Button>(R.id.btn_search) // 취소 버튼 ID
        btnCancel.setOnClickListener {
            // 아무 동작 없이 액티비티 종료
            finish()
        }

    }
}