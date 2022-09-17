package com.teamdefine.signease.loginandregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            loginUser(email, password)
        }
        return binding.root
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Logged In", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            activity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }
}