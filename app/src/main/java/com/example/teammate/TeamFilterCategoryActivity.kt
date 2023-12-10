package com.example.teammate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
class TeamFilterCategoryActivity : AppCompatActivity() {

    private lateinit var majorEditText: EditText
    private lateinit var btnShowResults: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_filter_category)


        majorEditText = findViewById(R.id.major_edittext)
        btnShowResults = findViewById(R.id.btn_cancel)

        btnShowResults.setOnClickListener {
            val selectedMajor = majorEditText.text.toString()
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