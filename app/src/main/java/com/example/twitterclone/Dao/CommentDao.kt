package com.example.twitterclone.Dao

import android.util.Log
import com.example.twitterclone.model.Comment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CommentDao {
    private var cuid = FirebaseAuth.getInstance().currentUser?.uid
    private val commentDb = FirebaseFirestore.getInstance().collection("comments")
    fun postComment(tweetId: String, userId: String, comment: String): Task<Void> {
        val id = UUID.randomUUID().toString()
        val createdAt = System.currentTimeMillis()
        val comment = Comment(comment, createdAt, cuid.toString())
        return commentDb.document(tweetId).collection(userId).document(id).set(comment)
    }

    fun getCommentsCollection(tweetId: String, uid: String): CollectionReference {
        return commentDb.document(tweetId).collection(uid)
    }
}