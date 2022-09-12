package com.teamdefine.signease.loginandregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    saveUserData(fullName, uid, currentUser.uid)
                }
            } else
                Toast.makeText(
                    activity,
                    task.exception!!.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    private fun saveUserData(fullName: String, uid: String, currentUser: String) {
        val database = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["fullName"] = fullName
        user["uid"] = uid

        database.collection("Users").document(currentUser).set(user).addOnSuccessListener {
            Toast.makeText(activity, "User registered", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}