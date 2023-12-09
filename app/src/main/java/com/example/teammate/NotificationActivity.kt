package com.example.teammate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class NotificationActivity : AppCompatActivity() {

    private var notificationEnable = 0 // 알림 상태 (0: 비활성화, 1: 활성화)
    private lateinit var btnNotification: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

      //  btnNotification = findViewById(R.id.btn_notification)
      // updateNotificationIcon() // 초기 상태 설정
    }

    //뒤로가기 버튼
    fun onNotificationBackButtonClick(view: View?){
        finish();
    }

    //notificationEnable 설정 (버튼 누르면 활성화/비활성화)
    fun onNotificationEnableButtonClick(view: View) {
        notificationEnable = if (notificationEnable == 0) 1 else 0
        updateNotificationIcon()
    }

    private fun updateNotificationIcon() {
        val iconResId = if (notificationEnable == 1) {
            R.drawable.ic_notification_active // 활성화된 알림 아이콘
        } else {
            R.drawable.ic_notification // 기본 알림 아이콘
        }
        btnNotification.setImageResource(iconResId)
    }
}
