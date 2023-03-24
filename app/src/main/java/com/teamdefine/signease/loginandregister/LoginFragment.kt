package com.teamdefine.signease.loginandregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding //binding
    private lateinit var firebaseAuth: FirebaseAuth //firebase auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance() //getting instance

        //on click of login button, login the user
        binding.loginButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) Toast.makeText(
                activity,
                "All fields are mandatory",
                Toast.LENGTH_SHORT
            ).show()
            else {
                binding.progressBar.visibility = View.VISIBLE
                loginUser(email, password)
            }
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.forgotPassword.setOnClickListener {
            if (binding.inputEmail.text.toString().isEmpty()) Toast.makeText(
                requireContext(),
                "Enter email first",
                Toast.LENGTH_SHORT
            ).show()
            else {
                binding.progressBar.visibility = View.VISIBLE
                sendResetPasswordEmail(binding.inputEmail.text.toString())
            }
        }

        return binding.root
    }

    private fun sendResetPasswordEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    requireContext(), "Reset password email sent", Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Some internal error occurred. Try again later.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //login user function
    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, "Logged In", Toast.LENGTH_SHORT).show()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomePageFragment())
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    activity, task.exception!!.message.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}