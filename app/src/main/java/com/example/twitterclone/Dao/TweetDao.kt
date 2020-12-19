package com.example.twitterclone.Dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.twitterclone.model.Tweet
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
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
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var result: Task<Void>
    fun postTweet(tweet: String) {
        val simpleDateFormat = SimpleDateFormat("MMM,d")
        val date = Date(System.currentTimeMillis())
        val createdAt = simpleDateFormat.format(date)
        db.document("$uid").get().addOnSuccessListener {
            val profileUrl = it.get("profileUrl").toString()
            val tweetEntity = Tweet(tweet, 0, 0, createdAt, profileUrl,uid)
            doPost(tweetEntity)
        }


    }

    private fun toast(message: String, toastContext: Context) {
        Toast.makeText(toastContext, message, Toast.LENGTH_SHORT).show()
    }

    fun getTweets(): DocumentReference {
        return db.document("tweets")
    }

    private fun doPost(tweetEntity: Tweet) {
        db.document("tweets").get().addOnSuccessListener {

            result = if (it.get(uid.toString()) == null) {
                val map = mapOf<String, ArrayList<Tweet>>(
                    uid.toString() to arrayListOf(tweetEntity)
                )
                db.document("tweets").set(map).addOnSuccessListener {
                    toast("Success", context)
                }.addOnFailureListener {
                    toast("Failure", context)
                }
            } else {

                db.document("tweets").update(uid.toString(), FieldValue.arrayUnion(tweetEntity))
                    .addOnSuccessListener {
                        toast("Success", context)
                    }.addOnFailureListener {
                    toast("Failure", context)
                }
            }
        }.addOnFailureListener {
            toast("Failure", context)
        }

    }
    fun updateLike(uid:String,position:Int){
        db.document("tweets").get().addOnSuccessListener {
            val tweetArray=it.get("$uid") as ArrayList<HashMap<String,Any>>
            val likes=tweetArray[position].get("likes") as Long
            postLike(likes,uid,position)
        }
    }
    private fun postLike(likes:Long,uid:String,position:Int){

    }
}