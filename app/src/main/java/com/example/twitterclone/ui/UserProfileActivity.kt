package com.example.twitterclone.ui

import android.content.Intent
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
import com.example.twitterclone.model.Tweet
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity(),ClickInterface {
    private val db=FirebaseFirestore.getInstance()
    lateinit var adapter: MyAdapter
    private val tweetDao=TweetDao(this)
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
        val query=tweetDao.getTweets().whereEqualTo("uid",uid)
        val options= FirestoreRecyclerOptions.Builder<Tweet>().setQuery(query, Tweet::class.java).build()
        adapter=MyAdapter(options,this)
        rev.adapter=adapter
        rev.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }

    override fun clickLike(tweetId: String) {
        val uid=FirebaseAuth.getInstance().currentUser?.uid
       tweetDao.postLike(tweetId,uid.toString())
    }

    override fun clickComment(tweetId: String,uid:String) {
        val intent= Intent(this,CommentActivity::class.java)
        intent.putExtra("uid",uid)
        intent.putExtra("tweetId",tweetId)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}