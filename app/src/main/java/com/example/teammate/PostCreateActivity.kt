package com.example.teammate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class PostCreateActivity : AppCompatActivity() {

    private lateinit var textView: TextView
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


    fun onPostCreateBackButtonClick(view: View?){
        finish()
    }
}