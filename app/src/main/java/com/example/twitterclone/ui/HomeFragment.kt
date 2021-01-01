package com.example.twitterclone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.MyAdapter
import com.example.twitterclone.interfaces.ClickInterface
import com.example.twitterclone.model.Tweet
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class HomeFragment : Fragment(), ClickInterface {
   lateinit var adapter:MyAdapter
    private val tweetDao by lazy {
        TweetDao(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val postButton = view.findViewById<FloatingActionButton>(R.id.postTweet)
        postButton.setOnClickListener {
            val intent = Intent(activity, TweetPostActivity::class.java)
            startActivity(intent)
        }

//        tweetDao.getTweets().whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid)
//            .addSnapshotListener { value, error ->
//                val list = value?.documents as ArrayList<DocumentSnapshot>
//                updateRecyclerView(list, view)
//
//            }
        val rev = view.findViewById<RecyclerView>(R.id.tweetsRecyclerView)
        val query=tweetDao.getTweets().orderBy("content")
        val options=FirestoreRecyclerOptions.Builder<Tweet>().setQuery(query,Tweet::class.java).build()
       adapter=MyAdapter(options,this)
        query.get().addOnSuccessListener {
            Log.e("Query","${it.documents}")
        }

        rev.adapter=adapter
        rev.layoutManager=LinearLayoutManager(requireContext())
        return view
    }

//    private fun updateRecyclerView(list: ArrayList<DocumentSnapshot>, view: View) {
//        val rev = view.findViewById<RecyclerView>(R.id.tweetsRecyclerView)
//        rev.adapter = MyAdapter(list, this)
//        rev.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun clickLike(tweetId: String) {
        Log.e("TweetId",tweetId)
        tweetDao.updateLike(tweetId)
    }

    override fun clickComment(tweetId: String, uid: String) {
        val intent=Intent(context,CommentActivity::class.java)
        intent.putExtra("tweetId",tweetId)
        intent.putExtra("uid",uid)
        startActivity(intent)
    }


}