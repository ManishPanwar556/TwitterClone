package com.example.twitterclone

data class User(
    var name:String?,
    val profileUrl:String,
    val followers:Int,
    val following:Int,
    val tweets:ArrayList<String>
)
