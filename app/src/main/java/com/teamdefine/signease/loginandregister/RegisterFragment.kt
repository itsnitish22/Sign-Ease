package com.teamdefine.signease.loginandregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.teamdefine.signease.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding //binding
    private lateinit var auth: FirebaseAuth //firebase auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        //on click of sign up button
        binding.signUpButton.setOnClickListener {
            val fullName = binding.inputName.text.toString()
            val uid = binding.inputUid.text.toString()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString()

            //toast message if any one of the field is empty
            if (fullName.isEmpty() || uid.isEmpty() || email.isEmpty() || password.isEmpty())
                Toast.makeText(activity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            else {
                binding.progressBar.visibility = View.VISIBLE
                registerUser(fullName, uid, email, password)
            }
        }
        binding.signIn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        return binding.root
    }

    //register the user on firebase (authenticate)
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
                    //once successfully authenticated, save user data to firestore
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

    //saving user data to firestore
    private fun saveUserData(fullName: String, uid: String, currentUser: String) {
        val database = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["fullName"] = fullName
        user["uid"] = uid

        //name of collection on firestore is "Users", and the document will be mapped to firebase authentication uid
        database.collection("Users").document(currentUser).set(user).addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(activity, "Registered Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}