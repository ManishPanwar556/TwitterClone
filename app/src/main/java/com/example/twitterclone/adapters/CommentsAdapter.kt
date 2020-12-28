package com.example.twitterclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.Dao.UserDao
import com.example.twitterclone.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CommentsAdapter(var list: ArrayList<DocumentSnapshot>) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private val userDao= UserDao()
    inner class CommentsViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val userImage = holder.view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.commentProfile)
        val userName = holder.view.findViewById<TextView>(R.id.commentUserName)
        val commentTextView = holder.view.findViewById<TextView>(R.id.commentTextView)
        val id=list.get(position).get("createdBy").toString()
        val comment=list.get(position).get("comment").toString()
        userDao.getUser(id).get().addOnSuccessListener {
            userName.text=it.get("name").toString()
            Glide.with(holder.view).load(it.get("profileUrl").toString()).into(userImage)
            commentTextView.text=comment
        }
    }

    override fun getItemCount() = list.size

}