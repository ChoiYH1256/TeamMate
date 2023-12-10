package com.example.teammate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationAdapter(private val applications: MutableList<Application>) : RecyclerView.Adapter<ApplicationAdapter.ViewHolder>() {

    // ViewHolder 클래스는 리사이클러뷰 항목의 뷰를 관리합니다.
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvApplicantName: TextView = view.findViewById(R.id.tvApplicantName)
        val tvSelfIntroduction: TextView = view.findViewById(R.id.tvSelfIntroduction)
        // 기타 뷰 바인딩
    }

    // 뷰 홀더를 생성합니다. 여기서 각 항목의 레이아웃을 지정합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.application_item_layout, parent, false)
        return ViewHolder(view)
    }

    // 각 항목에 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val application = applications[position]

        RetrofitClient.authService.getProfile(application.applicantid).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    holder.tvApplicantName.text = userProfile?.name
                } else {
                    // 서버 오류 메시지 표시
                    val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류 발생"
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                // 네트워크 오류 메시지 표시

            }
        })


       // holder.tvApplicantName.text = application.applicantid // 예시입니다. 실제로는 사용자 이름을 표시해야 합니다.
        holder.tvSelfIntroduction.text = application.selfIntroduction
        // 기타 데이터 바인딩
    }

    fun updateData(newApplications: List<Application>) {
        applications.clear()
        applications.addAll(newApplications)
        notifyDataSetChanged() // 리사이클러뷰를 업데이트합니다.
    }
    override fun getItemCount() = applications.size
}
