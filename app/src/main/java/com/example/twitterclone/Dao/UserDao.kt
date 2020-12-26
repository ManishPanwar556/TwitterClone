package com.example.twitterclone.Dao

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.twitterclone.model.User
import com.example.twitterclone.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao() {
    lateinit var context: Context
    constructor(context: Context) : this() {
        this.context=context
    }
    private val db = FirebaseFirestore.getInstance()

    fun addUser(firebaseUser: FirebaseUser?) {
        val name = firebaseUser?.displayName
        val uid = firebaseUser?.uid
        val url = firebaseUser?.photoUrl.toString()
        db.collection("users").document(firebaseUser?.uid!!).get().addOnSuccessListener {
            if (!it.exists()) {
                val user =
                    User(name, url, 0, 0)
                GlobalScope.launch(Dispatchers.IO) {
                    if (uid != null && name != null) {
                        db.collection("users").document(uid).set(user).addOnSuccessListener {
                            Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "User Failure", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
    fun getUser(id: String): DocumentReference {
        return db.collection("users").document(id)
    }

}