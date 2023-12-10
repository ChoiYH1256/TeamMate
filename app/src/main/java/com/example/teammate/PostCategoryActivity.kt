package com.example.teammate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class PostCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_category)
        val postId = getPostId() // 현재 보고 있는 게시물의 ID를 가져옵니다.


        val deleteButton = findViewById<AppCompatButton>(R.id.btn_delete)

        deleteButton.setOnClickListener {
            deletePost(postId)
        }
    }

    private fun deletePost(postId: String) {
        RetrofitClient.postService.deletePost(postId).enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    // 게시물 삭제 성공
                    Toast.makeText(this@PostCategoryActivity, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // 액티비티 종료 또는 목록으로 돌아가기
                } else {
                    // 오류 처리
                    Toast.makeText(this@PostCategoryActivity, "삭제 실패: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Toast.makeText(this@PostCategoryActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPostId(): String {
        // 게시물 ID를 반환하는 로직 구현
        // 예: 인텐트에서 게시물 ID를 가져오는 경우
        return intent.getStringExtra("POST_ID") ?: ""
    }
}