package com.example.teammate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomePostRecyclerviewAdapter(private val posts: List<Post>) : RecyclerView.Adapter<HomePostRecyclerviewAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvAnnouncement: TextView = view.findViewById(R.id.tv_announcement)

        fun bind(post: Post) {
            tvAnnouncement.text = post.title
            // 다른 뷰 바인딩 로직
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_home_recyclerview_item_announcement, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size
}
