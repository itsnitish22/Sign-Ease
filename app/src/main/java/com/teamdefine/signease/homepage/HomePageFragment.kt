package com.teamdefine.signease.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamdefine.signease.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var firebaseData = mutableMapOf<String, Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        val loggedIn = checkUser()
        if (loggedIn)
            getDataFromFirestore()
        else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        return binding.root
    }

    private fun getDataFromFirestore() {
        val database = FirebaseFirestore.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            database.collection("Users").document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        firebaseData = document.data as MutableMap<String, Any>
                        Log.i("HomePageFragment", "DocumentSnapshot: $firebaseData")
                        Toast.makeText(activity, "Data received from firestore", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun checkUser(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}