package com.example.teammate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TalentActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TalentRecyclerviewAdapter
    private val FILTER_REQUEST_CODE = 1
    private var allUsers: List<UserProfile> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talent)

        val toolbar: Toolbar = findViewById(R.id.btn_back)
        setSupportActionBar(toolbar)
        // 툴바에 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.rv_talent)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 초기 빈 어댑터 설정
        adapter = TalentRecyclerviewAdapter(emptyList())
        recyclerView.adapter = adapter



        RetrofitClient.authService.getAllUsers().enqueue(object : Callback<List<UserProfile>> {
            override fun onResponse(call: Call<List<UserProfile>>, response: Response<List<UserProfile>>) {
                if (response.isSuccessful) {
                    allUsers = response.body() ?: emptyList()
                    adapter = TalentRecyclerviewAdapter(allUsers)
                    recyclerView.adapter=adapter
                } else {
                    Log.e("TalentActivity", "오류: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<UserProfile>>, t: Throwable) {
                Log.e("TalentActivity", "네트워크 오류: ", t)
            }
        })



        val filterButton = findViewById<Button>(R.id.btn_filter) // 실제 버튼 ID로 변경
        filterButton.setOnClickListener {
            val intent = Intent(this, TeamFilterCategoryActivity::class.java)
            startActivityForResult(intent, FILTER_REQUEST_CODE)
        }

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val selectedMajor = it.getStringExtra("selectedMajor") ?: return
                // 필터링된 게시물을 로드하는 로직
                loadFilteredUsers(selectedMajor)
            }
        }
    }

    //수정해야함
    private fun loadFilteredUsers(selectedMajor: String) {
        val filteredUsers = allUsers.filter { UserProfile ->
            UserProfile.major == selectedMajor // `major`는 `Post` 클래스에 정의된 해당 전공을 나타내는 필드입니다.
        }
        adapter = TalentRecyclerviewAdapter(filteredUsers)
        recyclerView.adapter=adapter
        //setupRecyclerView(filteredPosts)
    }


}
