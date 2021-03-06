package com.example.twitterclone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.twitterclone.Dao.TweetDao
import com.example.twitterclone.databinding.ActivityTweetPostBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TweetPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTweetPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.postTweet.setOnClickListener {
            when {
                binding.tweetInputEditText.text.toString().length > 150 -> {
                    binding.tweetTextInput.error = "Tweet Size is greater than 150"
                }
                binding.tweetInputEditText.text.toString().isEmpty() -> {
                    binding.tweetTextInput.error = "Tweet Cannot be empty"
                }
                else -> {
                    //                Post the tweet
                    TweetDao(applicationContext).postTweet(binding.tweetInputEditText.text.toString())
                    finish()

                }
            }
        }
    }
}