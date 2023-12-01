package com.example.teammate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton


class PostActivity : AppCompatActivity() {

    private lateinit var backButtonPost: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //글 작성자인지 확인하는 로직
        //checkIfUserIsAuthor()

        // backButton_Post ID로 뒤로가기 버튼 참조
        backButtonPost = findViewById(R.id.backButtonPost)
        // 뒤로가기 버튼 클릭 리스너 설정
        backButtonPost.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }

    /* 글 작성자인지 확인하고 작성자면 delete button 활성화
    private fun checkIfUserIsAuthor() {
        val isAuthor = ... // 현재 사용자가 글 작성자인지 확인하는 로직

        if (isAuthor) {
            val deleteButton: Button = findViewById(R.id.btn_delete_post)
            deleteButton.visibility = View.VISIBLE
        }
    }
    */

    /* 글 삭제 로직
    fun onDeletePostClick(view: View) {
        // 글 삭제 로직 구현
    }
    */

    fun onChatClick(view: View) { //채팅창으로 넘어감
        // ChatActivity로 이동하는 인텐트를 생성합니다.
        val intent = Intent(this, ChatActivity::class.java)
        // ChatActivity로 이동합니다.
        startActivity(intent)
    }



}