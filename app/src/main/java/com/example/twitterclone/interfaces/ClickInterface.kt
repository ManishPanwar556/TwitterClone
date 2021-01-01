package com.example.twitterclone.interfaces

interface ClickInterface {
    fun clickLike(tweetId: String)
    fun clickComment(tweetId: String,uid:String)

}