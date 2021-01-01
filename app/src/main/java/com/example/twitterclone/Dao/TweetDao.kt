package com.example.twitterclone.Dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.twitterclone.model.Tweet
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TweetDao(private val context: Context) {

    val tweetTag = "Tweet"
    private val db = FirebaseFirestore.getInstance().collection("users")
    private val tweetDb=FirebaseFirestore.getInstance().collection("tweets")
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var result: Task<Void>
    fun postTweet(tweet: String) {

        db.document("$uid").get().addOnSuccessListener {
            val profileUrl = it.get("profileUrl").toString()
            val totalFollowers=it.get("followers") as Long
            val totalFollowing=it.get("following") as Long
            val name=it.get("name").toString()
            doPost(profileUrl,totalFollowers,totalFollowing,name,tweet)
        }


    }
    fun getLikes(tweetId: String):DocumentReference{
        return tweetDb.document(tweetId)
    }
    private fun toast(message: String, toastContext: Context) {
        Toast.makeText(toastContext, message, Toast.LENGTH_SHORT).show()
    }

    fun getTweets(): CollectionReference {
        return tweetDb
    }

    private fun doPost(profileUrl:String,totalFollowers:Long,totalFollowing:Long,name:String,tweet: String) {
        val id=UUID.randomUUID().toString()
        val simpleDateFormat = SimpleDateFormat("MMM d")
        val date = Date(System.currentTimeMillis())
        val createdAt = simpleDateFormat.format(date)
        val tweetEntity=Tweet(tweet,0,0,createdAt,profileUrl,uid!!,id,false)

       tweetDb.document("$id").set(tweetEntity).addOnSuccessListener {
           toast("Tweet Post Success",context)
       }.addOnFailureListener {
           toast("Tweet Post Failure",context)
       }
    }

    fun updateLike(tweetId:String) {
        tweetDb.document(tweetId).get().addOnSuccessListener {
            val like=it.get("likes") as Long + 1
            postLike(like,tweetId)
        }
         
    }
    private fun postLike(like:Long,tweetId: String){
        tweetDb.document(tweetId).update("likes",like)
    }


}