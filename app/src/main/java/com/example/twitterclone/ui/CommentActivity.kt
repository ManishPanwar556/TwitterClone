package com.example.twitterclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.Dao.CommentDao
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.Dao.UserDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.CommentsAdapter
import com.example.twitterclone.model.Comment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.Query

class CommentActivity : AppCompatActivity() {
    private val commentDao = CommentDao()
    private val userDao=UserDao()
    private val tweetDao= TweetDao(this)
    private lateinit var adapter: CommentsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        val tweetId = intent.extras?.get("tweetId").toString()
        val uid = intent.extras?.get("uid").toString()
        val killActivity=findViewById<ImageView>(R.id.killActivityBtn)
        val rev=findViewById<RecyclerView>(R.id.commentsRecyclerView)
        val editText=findViewById<EditText>(R.id.editText)
        val commentButton=findViewById<MaterialButton>(R.id.button)
        val profile=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.tweetProfile)
        val userName=findViewById<TextView>(R.id.tweeUserNameTextView)
        val tweet=findViewById<TextView>(R.id.tweetTextView)
        val commentsNumber=findViewById<TextView>(R.id.totalCommentsNumber)
        val thumbsUp=findViewById<ImageView>(R.id.commentThumbsUp)
        val totalLikes=findViewById<TextView>(R.id.likesNumber)
        val query=commentDao.getCommentsCollection(tweetId,uid).orderBy("createdAt",Query.Direction.DESCENDING)
        val options=FirestoreRecyclerOptions.Builder<Comment>().setQuery(query,Comment::class.java).build()
        adapter= CommentsAdapter(options)
        rev.adapter=adapter
        rev.layoutManager=LinearLayoutManager(this)
        thumbsUp.setOnClickListener {
            tweetDao.updateLike(tweetId)
        }
        tweetDao.getLikes(tweetId).addSnapshotListener { value, error ->
            totalLikes.text=value?.get("likes").toString()
        }
        userDao.getUser(uid).get().addOnSuccessListener {
            userName.text=it.get("name").toString()
            Glide.with(this).load(it.get("profileUrl").toString()).into(profile)
        }
        tweetDao.getTweets().document(tweetId).get().addOnSuccessListener {
            tweet.text=it.get("content").toString()
        }

        query.addSnapshotListener { value, error ->
          commentsNumber.text= "${value?.documents?.size.toString()} comments"
        }
        killActivity.setOnClickListener {
            finish()
        }
        commentButton.setOnClickListener {
            val comment=editText.text.toString()
            if(comment.isEmpty()){
                Toast.makeText(this,"Cannot Post an Empty Comment",Toast.LENGTH_SHORT).show()
            }
            else{
//                Post The Comment
                editText.text.clear()
                commentDao.postComment(tweetId,uid,comment).addOnSuccessListener {
                    Toast.makeText(this,"Comment Posted Successfully",Toast.LENGTH_SHORT).show()
                    rev.smoothScrollToPosition(0)
                }.addOnFailureListener {
                    Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()
                }
            }
        }
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