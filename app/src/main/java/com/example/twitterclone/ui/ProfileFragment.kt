package com.example.twitterclone.ui

import com.example.twitterclone.Dao.UserDao
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.twitterclone.R
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val userName = view.findViewById<TextView>(R.id.userName)
        val followers = view.findViewById<TextView>(R.id.followers)
        val following = view.findViewById<TextView>(R.id.following)
        val profileImage =
            view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profileImage)
        UserDao(requireContext()).getUser(FirebaseAuth.getInstance().currentUser?.uid.toString()).addSnapshotListener { value, error ->
            followers.text = value?.get("followers").toString()
            following.text = value?.get("following").toString()
            userName.text = value?.get("name").toString()
            activity?.applicationContext?.let {
                Glide.with(it).load(value?.get("profileUrl")).into(profileImage)
            }
        }
        return view
    }


}