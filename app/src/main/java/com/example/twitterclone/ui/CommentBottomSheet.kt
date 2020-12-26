package com.example.twitterclone.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.twitterclone.Dao.CommentDao
import com.example.twitterclone.R

import com.example.twitterclone.adapters.CommentsAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CommentBottomSheet(private val uid: String, private val tweetId: String) :
    BottomSheetDialogFragment() {
    private val commentDao = CommentDao()
    private val commentDb = FirebaseFirestore.getInstance().collection("comments")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater?.inflate(R.layout.bottom_sheet, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val btn = view?.findViewById<MaterialButton>(R.id.commentBtn)
        val editText = view?.findViewById<EditText>(R.id.commentEditText)
        val rev = view?.findViewById<RecyclerView>(R.id.recyclerView)
        commentDb.document(tweetId).collection(uid)
            .orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
                val list = value?.documents as ArrayList<DocumentSnapshot>
                Log.e("List","$list")
                rev?.adapter = CommentsAdapter(list)
                rev?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            }
        btn?.setOnClickListener {
            if (editText==null||editText.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "OOPS cannot post empty comment",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val comment = editText.text.toString()
                commentDao.postComment(tweetId, uid, comment).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



}