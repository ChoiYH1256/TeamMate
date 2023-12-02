package com.example.teammate

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MessageAdapter
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)



        // 뒤로가기 버튼 참조
        backButton = findViewById(R.id.backButtonChat)
        // 뒤로가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }


        messageEditText = findViewById(R.id.messageEditText)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        recyclerView = findViewById(R.id.chatRecyclerView)

        adapter = MessageAdapter(mutableListOf())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        sendMessageButton.setOnClickListener {
            val messageText = messageEditText.text.toString()

            //디버그 로그 확인
            //Log.d("ChatActivity", "Send Button Clicked: $messageText")

            if (messageText.isNotEmpty()) {
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                adapter.addMessage(Message(messageText, currentTime)) // 메시지와 시각을 포함하여 객체 생성
                messageEditText.text.clear()
                recyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }


}