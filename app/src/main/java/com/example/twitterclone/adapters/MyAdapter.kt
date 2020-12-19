package com.example.twitterclone.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.R
import com.example.twitterclone.interfaces.ClickInterface

class MyAdapter(private val list:ArrayList<HashMap<String,Any>>,private val clickInterface: ClickInterface):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        init {
            view.findViewById<ImageView>(R.id.totalLikes).setOnClickListener {
                if(adapterPosition!=RecyclerView.NO_POSITION){
                    clickInterface.clickLike(list.get(adapterPosition).get("uid").toString())
                }
            }
            view.findViewById<ImageView>(R.id.totalDisLikes).setOnClickListener {
                clickInterface.clickDislike(list.get(adapterPosition).get("uid").toString())
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_tweets_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        val tweet=holder.view.findViewById<TextView>(R.id.tweet)
        val like=holder.view.findViewById<TextView>(R.id.totalLikes)
        val dislike=holder.view.findViewById<TextView>(R.id.totalDisLikes)
        val profile=holder.view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.userProfile)
        tweet.text=list[position].get("content").toString()
        like.text=list[position].get("likes").toString()
        dislike.text=list[position].get("dislikes").toString()
        Glide.with(holder.view).load(list[position].get("profileUrl").toString()).into(profile)

    }

    override fun getItemCount() = list.size
}