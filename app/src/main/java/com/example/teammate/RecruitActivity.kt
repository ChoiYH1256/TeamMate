package com.example.teammate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class RecruitActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var posts: List<Post> = listOf() // Post는 사용자 정의 데이터 클래스
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruit)
    }
}