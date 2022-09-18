package com.teamdefine.signease.homepage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamdefine.signease.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: HomeFragmentViewModel
    var firebaseData = mutableMapOf<String, Any>() //global variable which will store user data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()

        //checking if user is logged in or not
        val loggedIn = checkUser()
        if (loggedIn) { //if logged in
            Handler(Looper.getMainLooper()).postDelayed({ //adding delay
                getDataFromFirestore() //getting data from firestore (user's name, uid)
            }, 4000)

            viewModel.getSignatureRequests() // getting all signature requests made till date
        } else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        //observing changes in received signature requests
        viewModel.requests.observe(requireActivity(), Observer { requests ->
            Log.i("Home Page Fragment", requests.toString())
        })

        return binding.root
    }

    //getting data from firestore
    private fun getDataFromFirestore() {
        val database = FirebaseFirestore.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            database.collection("Users").document(firebaseUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        firebaseData =
                            document.data as MutableMap<String, Any> //setting data to global variable
                        Log.i("HomePageFragment", "DocumentSnapshot: $firebaseData")
                        Toast.makeText(activity, "Data received from firestore", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    //checking and returning if the user is logged in or not
    private fun checkUser(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}