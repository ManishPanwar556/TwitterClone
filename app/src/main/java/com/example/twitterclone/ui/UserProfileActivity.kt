package com.example.twitterclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.Dao.UserDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.MyAdapter
import com.example.twitterclone.interfaces.ClickInterface
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity(),ClickInterface {
    private val db=FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val tweetDao=TweetDao(this)
        val userDao=UserDao()
        val profileImage=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.otherUserProfile)
        val backBtn=findViewById<ImageView>(R.id.backBtn)
        val rev=findViewById<RecyclerView>(R.id.userRecyclerView)
        val followers=findViewById<TextView>(R.id.totalFollowers)
        val following=findViewById<TextView>(R.id.totalFollowing)

        backBtn.setOnClickListener {
            finish()
        }
        val uid= intent.extras?.get("uid").toString()
        userDao.getUser(uid).get().addOnSuccessListener {
            followers.text=it.get("followers").toString()
            following.text=it.get("following").toString()
        }
        db.collection("users").document(uid).get().addOnSuccessListener {
            val url=it.get("profileUrl").toString()
            Glide.with(this).load(url).into(profileImage)
        }
        tweetDao.getTweets().whereEqualTo("uid",uid).addSnapshotListener { value, error ->
            var list=value?.documents as ArrayList<DocumentSnapshot>
            Log.e("list","$list")
            rev.adapter=MyAdapter(list,this)
            rev.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        }
    }

    override fun clickLike(tweetId: String) {
        Toast.makeText(this,"Like Click",Toast.LENGTH_SHORT).show()
    }

    override fun clickComment(tweetId: String,uid:String) {
        Toast.makeText(this,"Dislike Click",Toast.LENGTH_SHORT).show()
    }
}