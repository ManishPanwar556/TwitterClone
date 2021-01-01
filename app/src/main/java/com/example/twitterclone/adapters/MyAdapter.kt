package com.example.twitterclone.adapters

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.Dao.UserDao
import com.example.twitterclone.R
import com.example.twitterclone.interfaces.ClickInterface
import com.example.twitterclone.model.Tweet
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MyAdapter(
    options: FirestoreRecyclerOptions<Tweet>,
    private val clickInterface: ClickInterface
) : FirestoreRecyclerAdapter<Tweet, MyAdapter.TweetViewHolder>(options) {

    inner class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profile =
            view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.userProfile)
        val tweet = view.findViewById<TextView>(R.id.tweet)
        val like = view.findViewById<TextView>(R.id.totalLikes)
        val thumbsUp = view.findViewById<ImageView>(R.id.thumbsUp)
        val name = view.findViewById<TextView>(R.id.tweetUserName)
        val comment=view.findViewById<ImageView>(R.id.comments)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TweetViewHolder {
        val viewHolder = TweetViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_tweets_item, parent, false)
        )
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: TweetViewHolder,
        position: Int,
        model: Tweet
    ) {
        holder.tweet.text = model.content
        holder.like.text = model.likes.toString()
        UserDao().getUser(model.uid!!).get().addOnSuccessListener {
            holder.name.text = it.get("name").toString()
            Glide.with(holder.profile.context).load(it.get("profileUrl").toString())
                .into(holder.profile)
        }
        val tweetId = model.tweetId
        holder.thumbsUp.setOnClickListener {
            clickInterface.clickLike(tweetId)
        }
        holder.comment.setOnClickListener {
            clickInterface.clickComment(tweetId,model.uid)
        }
    }
}