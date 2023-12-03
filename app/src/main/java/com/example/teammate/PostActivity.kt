package com.example.teammate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar


class PostActivity : AppCompatActivity() {

    private var isBookmarked = false // 북마크 상태를 추적하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //글 작성자인지 확인하는 로직
        //checkIfUserIsAuthor()

        val toolbar: Toolbar = findViewById(R.id.btn_cancel) // Toolbar의 실제 ID를 확인하세요.
        setSupportActionBar(toolbar)
        // 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // ImageView 초기화 및 클릭 리스너 설정
        val ivBookmark: ImageView = findViewById(R.id.iv_bookmark)
        ivBookmark.setOnClickListener {
            toggleBookmark()
        }
    }
    fun onPostToChatButtonClick(view: View) { //채팅창으로 넘어감
        // ChatActivity로 이동하는 인텐트를 생성합니다.
        val intent = Intent(this, ChatActivity::class.java)
        // ChatActivity로 이동합니다.
        startActivity(intent)
    }

    //뒤로가기
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

    //북마크 토글
    private fun toggleBookmark() {
        isBookmarked = !isBookmarked // 북마크 상태 토글
        updateBookmarkIcon()
    }

    //북마크 상태 업데이트
    private fun updateBookmarkIcon() {
        val ivBookmark: ImageView = findViewById(R.id.iv_bookmark)
        if (isBookmarked) {
            ivBookmark.setImageResource(R.drawable.ic_bookmarked) // 활성화된 북마크 아이콘
        } else {
            ivBookmark.setImageResource(R.drawable.ic_bookmark) // 기본 북마크 아이콘
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
}