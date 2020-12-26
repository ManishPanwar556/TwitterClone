package com.example.twitterclone.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.R
import com.example.twitterclone.adapters.SearchAdapter
import com.example.twitterclone.interfaces.SearchClickInterface
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class SearchFragment : Fragment(),SearchClickInterface {
    private val user = FirebaseFirestore.getInstance().collection("users")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val noResult=view.findViewById<TextView>(R.id.noResult)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                progressBar?.visibility = View.VISIBLE
                searchUsers(query, progressBar,noResult)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return view
    }


    private fun searchUsers(query: String?, progressBar: ProgressBar?,noResult:TextView) {
        if (query != null) {
            val searchQuery=query.capitalizeWords()
            user.whereGreaterThanOrEqualTo("name", searchQuery).addSnapshotListener { value, error ->
                var list = value?.documents as ArrayList<DocumentSnapshot>
                var rev=view?.findViewById<RecyclerView>(R.id.searchProfile)
                if (list.isEmpty()) {
                    noResult.visibility = View.VISIBLE
                    progressBar?.visibility=View.GONE
                    rev?.visibility=View.GONE
                } else {
                    noResult.visibility=View.GONE
                    updateRecyclerView(list,progressBar,rev)
                }
            }
        }

    }

    private fun updateRecyclerView(
        list: ArrayList<DocumentSnapshot>,
        progressBar: ProgressBar?
    ,rev: RecyclerView?
    ) {
        progressBar?.visibility = View.GONE
        rev?.visibility=View.VISIBLE
        rev?.adapter = SearchAdapter(list,this)
        rev?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    }
    private fun String.capitalizeWords()= split(" ").map { it.toLowerCase().capitalize() }.joinToString(" ")
    override fun onClick(uid: String) {
        val intent= Intent(context,UserProfileActivity::class.java)
        intent.putExtra("uid",uid)
        startActivity(intent)
    }
}