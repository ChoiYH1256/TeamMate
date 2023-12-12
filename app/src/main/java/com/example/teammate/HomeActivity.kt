package com.example.teammate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_fragment)

        val ivTeam = findViewById<ImageView>(R.id.iv_team)
        val ivTalent = findViewById<ImageView>(R.id.iv_talent)
        val ivAccount = findViewById<ImageView>(R.id.iv_account)

        // ImageView에 클릭 리스너 설정
        ivTeam.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // 클릭 이벤트 처리
                val intent = Intent(this@HomeActivity, RecruitActivity::class.java)
                startActivity(intent)
            }
        })

        ivTalent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // 클릭 이벤트 처리
                val intent = Intent(this@HomeActivity, TalentActivity::class.java)
                startActivity(intent)
            }
        })

        ivAccount.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // 클릭 이벤트 처리
                val intent = Intent(this@HomeActivity, MyPageActivity::class.java)
                startActivity(intent)
            }
        })




        // Toolbar 초기화 및 설정
        toolbar = findViewById(R.id.tool_bar) // toolbar ID를 확인하세요.
        setSupportActionBar(toolbar)

        RetrofitClient.postService.getAllPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body() ?: emptyList()
                    setupRecyclerView(posts)
                } else {
                    Log.e("GetPostsError", "오류: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.e("GetPostsError", "네트워크 오류: ", t)
            }
        })

        val tvMoreAnnouncement = findViewById<TextView>(R.id.tv_moreAnnouncement)
        tvMoreAnnouncement.setOnClickListener(View.OnClickListener {
            // 다른 액티비티로 이동하는 코드 작성
            val intent = Intent(this, RecruitActivity::class.java)
            startActivity(intent)
        })


    }

    private fun setupRecyclerView(posts: List<Post>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_announcement)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PostRecyclerviewAdapter(posts) { post ->
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("POST_ID", post.postId)
            Log.d("PostActivity", "Post ID: $post")
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_home_noti -> {
                Log.d("HomeFragment", "Notification button clicked")
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}