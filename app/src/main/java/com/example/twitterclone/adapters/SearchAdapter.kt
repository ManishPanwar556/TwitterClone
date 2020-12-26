package com.example.twitterclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitterclone.R
import com.example.twitterclone.interfaces.SearchClickInterface
import com.google.firebase.firestore.DocumentSnapshot

class SearchAdapter(private var list:ArrayList<DocumentSnapshot>,private var searchClickInterface: SearchClickInterface):RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    inner class SearchViewHolder(val view: View):RecyclerView.ViewHolder(view){
            init {
                view.setOnClickListener {
                    if(adapterPosition!=RecyclerView.NO_POSITION){
                        searchClickInterface.onClick(list[adapterPosition].id)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val profile=holder.view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.othersProfile)
        val nameTextView=holder.view.findViewById<TextView>(R.id.othersName)
        val profileUrl=list[position].get("profileUrl").toString()
        val name=list[position].get("name").toString()
        nameTextView.text=name
        Glide.with(holder.view).load(profileUrl).into(profile)
    }

    override fun getItemCount()=list.size
}