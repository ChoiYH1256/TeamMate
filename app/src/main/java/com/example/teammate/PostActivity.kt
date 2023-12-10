package com.example.teammate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostActivity : AppCompatActivity() {

    private var isBookmarked = false // 북마크 상태를 추적하는 변수
    private lateinit var current_uid: String
    private var post_uid: String? = null// post_uid를 nullable String?으로 선언

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvNickname: TextView

    private lateinit var post_Id: String
    private lateinit var post_Uid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        current_uid = getSavedUid() ?:return
        //글 작성자인지 확인하는 로직
        //checkIfUserIsAuthor()
        // 게시글 정보를 받아와서 멤버 변수에 저장

        val toolbar: Toolbar = findViewById(R.id.btn_cancel) // Toolbar의 실제 ID를 확인하세요.
        setSupportActionBar(toolbar)
        // 뒤로 가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 글 로직
        tvTitle = findViewById(R.id.tv_title)
        tvContent = findViewById(R.id.tv_content)
        tvNickname = findViewById(R.id.tv_nickname)
        val postId = intent.getStringExtra("POST_ID") ?: return
        Log.d("PostActivity", "Post ID: $postId")
        loadPost(postId)

        // ImageView 초기화 및 클릭 리스너 설정
        val ivBookmark: ImageView = findViewById(R.id.iv_bookmark)
        ivBookmark.setOnClickListener {
            if (postId != null) {
                // PostCategoryActivity를 시작하고 post_id를 전달합니다.
                val intent = Intent(this@PostActivity, PostCategoryActivity::class.java)
                intent.putExtra("POST_ID", postId)
                startActivity(intent)
            } else {
                // post_uid가 null일 경우 처리할 내용을 여기에 추가하세요.
            }
        }


    }

    fun onApplyButtonClick(view: View){
        val intent = Intent(this, MatchWritingActivity::class.java)
        intent.putExtra("POST_ID", post_Id)
        intent.putExtra("POST_UID", post_Uid)
        startActivity(intent)
    }


    private fun loadPost(postId: String) {
        RetrofitClient.postService.getPost(postId).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val statusCode = response.code() // HTTP 상태 코드
                Log.d("Response", "서버응답: ${response.body()}")
                Log.d("Response", "오류: $statusCode")
                if (response.isSuccessful) {
                    val post = response.body()!!

                    post_Id = post.postId
                    post_Uid = post.uid


                    tvTitle.text = post.title
                    tvContent.text = post.content
                    post_uid = post.uid // 게시물의 ID를 post_uid에 할당
                    // 사용자 프로필 요청
                    RetrofitClient.authService.getProfile(post.uid).enqueue(object : Callback<UserProfile> {
                        override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                            if (response.isSuccessful) {
                                val user = response.body()!!
                                tvNickname.text = user.name
                            } else {
                                // 오류 처리
                                Log.e("PostActivity", "오류: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                            Log.e("PostActivity", "네트워크 오류: ", t)
                        }
                    })
                } else {
                    // 오류 처리
                    Log.e("PostActivity", "오류: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("PostActivity", "네트워크 오류: ", t)
            }
        })
    }


    fun onPostToChatButtonClick(view: View) { //채팅창으로 넘어감

       // val intent = Intent(this, ChatActivity::class.java)
       // startActivity(intent)
        createChatRoom(post_uid,current_uid)


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
//    private fun toggleBookmark() {
//        isBookmarked = !isBookmarked // 북마크 상태 토글
//        updateBookmarkIcon()
//    }

    //북마크 상태 업데이트
//    private fun updateBookmarkIcon() {
//        val ivBookmark: ImageView = findViewById(R.id.iv_bookmark)
//        if (isBookmarked) {
//            ivBookmark.setImageResource(R.drawable.ic_bookmarked) // 활성화된 북마크 아이콘
//        } else {
//            ivBookmark.setImageResource(R.drawable.ic_bookmark) // 기본 북마크 아이콘
//        }
//    }

    fun createChatRoom(postUid: String?, currentUserUid: String?) {
        val request = CreateChatRoomRequest(currentUserUid, postUid)
        RetrofitClient.chatService.createChatRoom(request).enqueue(object : Callback<CreateChatRoomResponse> {
            override fun onResponse(call: Call<CreateChatRoomResponse>, response: Response<CreateChatRoomResponse>) {
                val statusCode = response.code() // HTTP 상태 코드
                Log.d("Response", "서버응답: ${response.body()}")
                Log.d("Response", "오류: $statusCode")
                if (response.isSuccessful) {
                    val chatRoomId = response.body()?.chatRoomId
                    // 채팅방 화면으로 이동하는 코드. 예: ChatActivity 시작
                    val intent = Intent(this@PostActivity, ChatActivity::class.java)
                    intent.putExtra("CHAT_ROOM_ID", chatRoomId)
                    startActivity(intent)
                } else {
                    // 에러 처리
                }
            }
            override fun onFailure(call: Call<CreateChatRoomResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("CreateChatError", "로그인 요청 실패: ", t) //디버깅용
                Toast.makeText(applicationContext, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun getSavedUid(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", null)
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