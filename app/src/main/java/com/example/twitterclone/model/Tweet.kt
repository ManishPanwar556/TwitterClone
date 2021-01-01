package com.example.twitterclone.model

data class Tweet(
    var content: String="",
    var likes:Long=0L,
    val dislikes:Long=0L,
    var createdAt:String="",
    val profileUrl:String="",
    val uid:String="",
    val tweetId:String="",
    val checkLike:Boolean=false
)