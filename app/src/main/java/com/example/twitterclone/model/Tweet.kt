package com.example.twitterclone.model

data class Tweet(
    var content: String?,
    var likes: Long?,
    val dislikes: Long?,
    var createdAt: String?,
    val profileUrl:String?,
    val uid:String?,
    val tweetId:String,
    val checkLike:Boolean
)