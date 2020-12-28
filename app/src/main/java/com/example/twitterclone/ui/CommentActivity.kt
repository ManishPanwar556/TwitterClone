package com.example.twitterclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.Dao.CommentDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.CommentsAdapter
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class CommentActivity : AppCompatActivity() {
    private val commentDao = CommentDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        val tweetId = intent.extras?.get("tweetId").toString()
        val uid = intent.extras?.get("uid").toString()
        val rev=findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val killActivity=findViewById<ImageView>(R.id.killActivityBtn)
        commentDao.getCommentsCollection(tweetId, uid)
            .orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            val list=value?.documents as ArrayList<DocumentSnapshot>
                rev.adapter=CommentsAdapter(list)
                rev.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        }
        killActivity.setOnClickListener {
            finish()
        }
    }
}