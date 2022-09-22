package com.teamdefine.signease.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.databinding.FragmentSplashScreenBinding
import com.teamdefine.signease.homepage.HomePageFragment

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
        if (loggedIn) {
            Handler().postDelayed({
                findNavController().navigate(
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToHomePageFragment()
                )
            }, 3000)
        } else {
            Handler().postDelayed({
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToRegisterFragment())
            }, 3000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToRegisterFragment())
        }

        return binding.root
    }
}