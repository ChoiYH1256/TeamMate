package com.example.teammate

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
                    val users = response.body() ?: emptyList()
                    adapter = TalentRecyclerviewAdapter(users)
                    recyclerView.adapter=adapter
                } else {
                    Log.e("TalentActivity", "오류: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<UserProfile>>, t: Throwable) {
                Log.e("TalentActivity", "네트워크 오류: ", t)
            }
        })

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
