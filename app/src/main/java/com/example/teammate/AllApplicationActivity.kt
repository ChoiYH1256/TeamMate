package com.example.teammate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllApplicationActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApplicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_application)

        val postId = intent.getStringExtra("POST_ID") ?: return

        recyclerView = findViewById(R.id.rv_application)
        adapter = ApplicationAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // 레이아웃 매니저 설정

        // Retrofit을 이용하여 특정 게시글에 대한 모든 지원서를 가져옵니다.
        RetrofitClient.postService.getPostApplications(postId).enqueue(object : Callback<List<Application>> {
            override fun onResponse(call: Call<List<Application>>, response: Response<List<Application>>) {
                if (response.isSuccessful) {
                    // 성공적으로 데이터를 받아온 경우
                    val applications = response.body() ?: emptyList()
                    adapter.updateData(applications)
                } else {
                    // 에러 처리
                    Log.e("AllApplicationActivity", "서버에서 데이터를 가져오는데 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<List<Application>>, t: Throwable) {
                // 네트워크 에러 처리
                Log.e("AllApplicationActivity", "네트워크 오류: ${t.message}")
            }
        })


    }



}