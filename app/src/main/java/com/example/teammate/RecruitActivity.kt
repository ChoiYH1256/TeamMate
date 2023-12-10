package com.example.teammate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecruitActivity : AppCompatActivity() {
    private val FILTER_REQUEST_CODE = 1

    private var allPosts: List<Post> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruit) // 레이아웃 파일 확인 필요

        RetrofitClient.postService.getAllPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    allPosts = response.body() ?: emptyList()
                    setupRecyclerView(allPosts)
                } else {
                    Log.e("GetPostsError", "오류: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("GetPostsError", "네트워크 오류: ", t)
            }
        })

        val filterButton = findViewById<Button>(R.id.btn_filter) // 실제 버튼 ID로 변경
        filterButton.setOnClickListener {
            val intent = Intent(this, TeamFilterCategoryActivity::class.java)
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }
        val toolbar = findViewById<Toolbar>(R.id.btn_back)
        setSupportActionBar(toolbar)

        // 뒤로가기 아이콘 클릭 이벤트 처리
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed() // 또는 다른 원하는 동작 수행
        }

    }

    private fun setupRecyclerView(posts: List<Post>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_arena) // RecyclerView ID 확인 필요
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostRecyclerviewAdapter(posts) { post ->
            val intent = Intent(this, PostActivity::class.java) // 상세 게시물 보기 액티비티
            intent.putExtra("POST_ID", post.postId)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val selectedMajor = it.getStringExtra("selectedMajor") ?: return
                // 필터링된 게시물을 로드하는 로직
                loadFilteredPosts(selectedMajor)
            }
        }
    }

    //수정해야함
    private fun loadFilteredPosts(selectedMajor: String) {
        val filteredPosts = allPosts.filter { post ->
            post.major == selectedMajor // `major`는 `Post` 클래스에 정의된 해당 전공을 나타내는 필드입니다.
        }
        setupRecyclerView(filteredPosts)
    }

}