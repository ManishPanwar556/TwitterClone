package com.example.twitterclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.Dao.UserDao
import com.example.twitterclone.R
import com.example.twitterclone.model.Comment
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CommentsAdapter(options: FirestoreRecyclerOptions<Comment>) :
    FirestoreRecyclerAdapter<Comment,CommentsAdapter.CommentsViewHolder>(options) {
    private val userDao= UserDao()
    inner class CommentsViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val commentProfile=view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.commentProfile)
        val nameTextView=view.findViewById<TextView>(R.id.commentUserName)
        val comment=view.findViewById<TextView>(R.id.commentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.comments_item,parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int, model: Comment) {
        userDao.getUser(model.createdBy).get().addOnSuccessListener {
            Glide.with(holder.view).load(it.get("profileUrl").toString()).into(holder.commentProfile)
            holder.nameTextView.text=it.get("name").toString()
        }
        holder.comment.text=model.comment
    }


}