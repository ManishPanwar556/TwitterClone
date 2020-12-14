package com.example.twitterclone.Dao

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class TweetDao {
    val tweetTag="Tweet"
    private val db=FirebaseFirestore.getInstance().collection("users")

    fun postTweet(tweet:String): Deferred<Task<Void>> {
        val res=GlobalScope.async(Dispatchers.IO) {
            db.document("${FirebaseAuth.getInstance().currentUser?.uid}").update("tweets",
                FieldValue.arrayUnion(tweet))

        }
        return res
    }
}