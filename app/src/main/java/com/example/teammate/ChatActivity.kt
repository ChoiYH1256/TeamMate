package com.example.teammate

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException


class ChatActivity : AppCompatActivity() {

    //나의 uid
    private lateinit var currentUserUid: String

    private lateinit var chatRoomId: String
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        currentUserUid = getSavedUid() ?: return // 현재 사용자의 UID를 가져옵니다.

        val toolbar: Toolbar = findViewById(R.id.btn_cancel)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed() // 현재 Activity에서 뒤로 가기
        }

        chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: return
        Log.d("chatRoomId", "$chatRoomId")
        recyclerView = findViewById(R.id.chatRecyclerView)
        messageEditText = findViewById(R.id.messageEditText)
        sendMessageButton = findViewById(R.id.sendMessageButton)

        messageAdapter = ChatMessageAdapter(mutableListOf(),currentUserUid)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadChatMessages(chatRoomId)

        sendMessageButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(chatRoomId, messageText)
                messageEditText.text.clear()
            }
        }

        /////////////////// 수정할 예정 /////////////////////

      //  messageEditText = findViewById(R.id.messageEditText)
       // sendMessageButton = findViewById(R.id.sendMessageButton)
       // recyclerView = findViewById(R.id.chatRecyclerView)

      //  adapter = MessageAdapter(mutableListOf())
      //  recyclerView.adapter = adapter
      //  recyclerView.layoutManager = LinearLayoutManager(this)

      //  sendMessageButton.setOnClickListener {
         //   val messageText = messageEditText.text.toString()

            //디버그 로그 확인
            //Log.d("ChatActivity", "Send Button Clicked: $messageText")

         //   if (messageText.isNotEmpty()) {
         //       val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

             //   adapter.addMessage(Message(messageText, currentTime)) // 메시지와 시각을 포함하여 객체 생성
            //    messageEditText.text.clear()
               // recyclerView.scrollToPosition(adapter.itemCount - 1)
      //    }
      //}
    }

    ///////////////////////// 채팅방 로직 ////////////////////////
    private fun loadChatMessages(chatRoomId: String) {
        RetrofitClient.chatService.getChatRoomMessages(chatRoomId).enqueue(object : Callback<GetChatRoomMessagesResponse> {
            override fun onResponse(call: Call<GetChatRoomMessagesResponse>, response: Response<GetChatRoomMessagesResponse>) {
                val statusCode = response.code() // HTTP 상태 코드
                Log.d("Response", "서버응답: ${response.body()}")
                Log.d("Response", "오류: $statusCode")
                if (response.isSuccessful) {
                    val messagesResponse = response.body()
                    Log.d("Response", "오류: $messagesResponse")
                    val chatMessages = messagesResponse?.messages?.map { message ->
                        val text = message.message ?: "메시지 없음"
                        ChatMessage(text,message.sender, formatTimestamp(message.timestamp))
                    } ?: emptyList()

                    chatMessages.forEach { message ->
                        messageAdapter.addMessage(message)
                    }
                    recyclerView.scrollToPosition(chatMessages.size - 1)
                } else {
                    // 에러 처리
                }
            }

            override fun onFailure(call: Call<GetChatRoomMessagesResponse>, t: Throwable) {
                Log.e("GetChatError", "채팅불러오기 실패: ", t) //디버깅용
                Toast.makeText(applicationContext, "네트워크 오류", Toast.LENGTH_SHORT).show()
                // 네트워크 오류 처리
            }
        })
    }

    private fun formatTimestamp(timestamp: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm aa", Locale.getDefault())
            val parsedDate: Date = inputFormat.parse(timestamp) ?: return "Unknown"
            return outputFormat.format(parsedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "Unknown"
        }
    }


    private fun sendMessage(chatRoomId: String, message: String) {
        val sendMessageRequest = SendMessageRequest(chatRoomId, currentUserUid, message)
        RetrofitClient.chatService.sendMessage(sendMessageRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 메시지 전송 성공 처리
                    val sentMessage = ChatMessage(message, currentUserUid, getCurrentTimestamp())
                    messageAdapter.addMessage(sentMessage)
                    recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
                } else {
                    // 에러 처리
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    private fun getCurrentTimestamp(): String {
        // 현재 시간을 문자열로 변환하여 반환합니다.
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())

    }









    private fun getSavedUid(): String? {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("uid", null)
        }
}