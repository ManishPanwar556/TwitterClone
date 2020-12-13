package com.example.twitterclone

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_home, container, false)
        val postButton=view.findViewById<MaterialButton>(R.id.postTweet)
        postButton.setOnClickListener {
            val intent= Intent(activity,TweetPostActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}