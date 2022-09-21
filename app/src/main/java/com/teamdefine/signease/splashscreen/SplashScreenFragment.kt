package com.teamdefine.signease.splashscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.databinding.FragmentSplashScreenBinding
import com.teamdefine.signease.homepage.HomePageFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SplashScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth //firebase auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance() //getting instance
        val loggedIn = HomePageFragment().checkUser(firebaseAuth)
        Log.i("helloabc", loggedIn.toString())
        if (loggedIn) {
            findNavController().navigate(
                SplashScreenFragmentDirections.actionSplashScreenFragmentToHomePageFragment()
            )
        } else {
            Log.i("helloabc", loggedIn.toString())
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToRegisterFragment())
        }
        return binding.root
    }
}