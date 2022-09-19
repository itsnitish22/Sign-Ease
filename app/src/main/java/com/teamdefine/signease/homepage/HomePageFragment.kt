package com.teamdefine.signease.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
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
            viewModel.getSignatureRequests() // getting all signature requests made till date
            viewModel.getDataFromFirestore()
        } else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        //observing changes in received signature requests
        viewModel.requests.observe(requireActivity()) { requests ->
            if (binding.swipeRefresh.isRefreshing)
                binding.swipeRefresh.isRefreshing = false
            Log.i("Home Page Fragment 1", requests.toString())
        }

        viewModel.data.observe(requireActivity()) { data ->
            firebaseData = data
            Log.i("Home Page Frag 2", firebaseData.toString())
        }

        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSignatureRequests()
        }

        return binding.root
    }

    //checking and returning if the user is logged in or not
    private fun checkUser(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}