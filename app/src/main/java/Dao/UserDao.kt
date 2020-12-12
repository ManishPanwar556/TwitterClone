package Dao

import com.example.twitterclone.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()

    fun addUser(firebaseUser: FirebaseUser?) {
        db.collection("users").document(firebaseUser?.uid!!).get().addOnSuccessListener {
            if(!it.exists()){
                val user =
                    User(firebaseUser?.displayName, firebaseUser?.photoUrl.toString(), 0, 0, arrayListOf())
                GlobalScope.launch(Dispatchers.IO) {
                    if (firebaseUser?.uid != null) {
                        db.collection("users").document(firebaseUser?.uid).set(user)
                    }
                }
            }
        }

    }
    fun getUser(): DocumentReference {
       return  db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
    }

}