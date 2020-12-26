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
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.R
import com.example.twitterclone.adapters.MyAdapter
import com.example.twitterclone.interfaces.ClickInterface
import com.example.twitterclone.model.Tweet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.GlobalScope

class HomeFragment : Fragment(), ClickInterface {

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

        tweetDao.getTweets().whereEqualTo("uid", FirebaseAuth.getInstance().currentUser?.uid)
            .addSnapshotListener { value, error ->
                val list = value?.documents as ArrayList<DocumentSnapshot>
                updateRecyclerView(list, view)

            }
        return view
    }

    private fun updateRecyclerView(list: ArrayList<DocumentSnapshot>, view: View) {
        val rev = view.findViewById<RecyclerView>(R.id.tweetsRecyclerView)
        rev.adapter = MyAdapter(list, this)
        rev.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun clickLike(tweetId: String) {
        tweetDao.getTweets().document(tweetId).get().addOnSuccessListener {
            var likes = it.get("likes") as Long + 1
            tweetDao.getTweets().document(tweetId).update("likes", likes).addOnSuccessListener {
                Toast.makeText(context, "Like Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Like Failure", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun clickComment(tweetId: String, uid: String) {
        CommentBottomSheet(uid, tweetId).show(requireActivity().supportFragmentManager, "tag")
    }


}