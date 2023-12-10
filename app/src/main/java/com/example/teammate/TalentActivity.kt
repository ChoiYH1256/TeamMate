package com.example.teammate

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class TalentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talent)

        val toolbar: Toolbar = findViewById(R.id.btn_back)
        setSupportActionBar(toolbar)

        // 툴바에 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 뒤로 가기 버튼을 눌렀을 때 동작
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}
