package com.example.teammate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatMessageAdapter(private val messages: MutableList<ChatMessage>, private val currentUserUid: String) : RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
       // val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutRes = if (viewType == VIEW_TYPE_MY_MESSAGE) {
            R.layout.my_message_layout // 나의 메시지 레이아웃 파일
        } else {
            R.layout.their_message_layout // 상대방 메시지 레이아웃 파일
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.message
      //  holder.timeTextView.text = message.timestamp
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.sender == currentUserUid) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_THEIR_MESSAGE
        }
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_THEIR_MESSAGE = 2
    }
}

data class ChatMessage(val message: String, val sender: String, val timestamp: String)
