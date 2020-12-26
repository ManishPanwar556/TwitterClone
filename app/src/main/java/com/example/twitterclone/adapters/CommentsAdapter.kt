package com.example.twitterclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CommentsAdapter(var list:ArrayList<DocumentSnapshot>):RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    inner class CommentsViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.comments_item,parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val profile=holder.view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.commentUserProfile)
        val name=holder.view.findViewById<TextView>(R.id.commentTextView)
        val commentTextView=holder.view.findViewById<TextView>(R.id.commentTextView)
        val userId=list[position].get("createdBy").toString()
        val comment=list[position].get("comment").toString()
        FirebaseFirestore.getInstance().collection("users").document(userId).get().addOnSuccessListener {
            Glide.with(holder.view).load(it.get("profileUrl").toString()).into(profile)
            name.text=it.get("name").toString()
        }
        commentTextView.text=comment

    }

    override fun getItemCount()=list.size

}