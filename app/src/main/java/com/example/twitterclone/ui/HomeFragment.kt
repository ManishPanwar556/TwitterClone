package com.example.twitterclone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.MyAdapter
import com.example.twitterclone.model.Tweet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.GlobalScope

class HomeFragment : Fragment() {

private val tweetDao by lazy {
    TweetDao(requireContext())
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_home, container, false)
        val postButton=view.findViewById<FloatingActionButton>(R.id.postTweet)
        postButton.setOnClickListener {
            val intent= Intent(activity, TweetPostActivity::class.java)
            startActivity(intent)
        }

        tweetDao.getTweets().addSnapshotListener{value,error->
            var list=value?.get(FirebaseAuth.getInstance().currentUser?.uid!!) as ArrayList<HashMap<String,Any>>
            updateRecyclerView(list,view)
//            Toast.makeText(context,"${list.get(0).get("content")}",Toast.LENGTH_SHORT).show()
        }
        return view
    }
    private fun updateRecyclerView(list:ArrayList<HashMap<String,Any>>,view: View){
        val rev=view.findViewById<RecyclerView>(R.id.tweetsRecyclerView)
        rev.adapter=MyAdapter(list)
        rev.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,false)
    }




}