package com.example.teammate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TalentRecyclerviewAdapter(private val users: List<UserProfile>) : RecyclerView.Adapter<TalentRecyclerviewAdapter.PersonViewHolder>() {

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val profileImageView: AppCompatImageView = view.findViewById(R.id.iv_profile)
        val nameTextView: TextView = view.findViewById(R.id.tv_name)
        val majorTextView: TextView = view.findViewById(R.id.tv_major)
        val universityTextView: TextView = view.findViewById(R.id.tv_university)

        fun bind(UserProfile: UserProfile) {
            // profileImageView에 이미지 설정 필요, 예를 들어 Glide 라이브러리 사용
            nameTextView.text = UserProfile.name
            majorTextView.text = UserProfile.major
            universityTextView.text = UserProfile.university
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_talent_recyclerview_item, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount() = users.size
}
