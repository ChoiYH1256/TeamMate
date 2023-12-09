package com.example.teammate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar

class NotificationActivity : AppCompatActivity() {

    private var notificationEnable = 0 // 알림 상태 (0: 비활성화, 1: 활성화)
    private lateinit var btnNotification: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val toolbar = findViewById<Toolbar>(R.id.btn_cancel)
        setSupportActionBar(toolbar)

        // 뒤로 가기 아이콘 클릭 시 액티비티 종료
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            finish()
        })
    }

    //뒤로가기 버튼


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
