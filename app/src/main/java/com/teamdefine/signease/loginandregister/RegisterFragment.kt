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
import com.teamdefine.signease.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth //firebase auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.registerButton.setOnClickListener {
            val fullName = binding.editTextTextPersonFullName.text.toString()
            val uid = binding.editTextUID.text.toString()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            registerUser(fullName, uid, email, password)

        }
        return binding.root
    }

    private fun registerUser(
        fullName: String,
        uid: String,
        email: String,
        password: String
    ) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful)
                    Toast.makeText(activity, "User Registered", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(
                        activity,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
            }
        )
    }
}