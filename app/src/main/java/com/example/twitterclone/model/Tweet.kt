package com.example.twitterclone.model

data class Tweet(
    var content: String?,
    var likes: Long?,
    val dislikes: Long?,
    var createdAt: String?
)