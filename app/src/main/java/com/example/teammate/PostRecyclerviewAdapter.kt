package com.example.teammate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostRecyclerviewAdapter(private val posts: List<Post>, private val onItemClicked: (Post) -> Unit) : RecyclerView.Adapter<PostRecyclerviewAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_home_recyclerview_item_announcement, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.itemView.setOnClickListener { onItemClicked(post) }
        holder.bind(post)
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvAnnouncement: TextView = view.findViewById(R.id.tv_announcement)

        fun bind(post: Post) {
            tvAnnouncement.text = post.title
        }
    }

    override fun getItemCount() = posts.size
}
