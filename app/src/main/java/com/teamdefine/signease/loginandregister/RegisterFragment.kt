package com.teamdefine.signease.loginandregister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.teamdefine.signease.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding //binding
    private lateinit var auth: FirebaseAuth //firebase auth
    private lateinit var viewModel: RegisterViewModel
    private var clientId: String = ""
    private var fullName: String = ""
    private var uid: String = ""
    private var email: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]

        viewModel.appResponse.observe(requireActivity()) { response ->
            clientId = response.api_app.client_id
            binding.progressBar.visibility = View.VISIBLE
            registerUser(fullName, uid, email, password, clientId)
        }
        viewModel.deleteClient.observe(requireActivity()) {
            if (it == true) {
                binding.progressBar.visibility = View.GONE
                binding.inputPassword.setText("")
                binding.inputPassword.requestFocus()
                view?.showKeyboard()
            }
        }
        viewModel.clientError.observe(requireActivity()){
            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
            binding.inputUid.setText("")
            binding.inputUid.requestFocus()
            view?.showKeyboard()
        }
        //on click of sign up button
        binding.signUpButton.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            fullName = binding.inputName.text.toString()
            uid = binding.inputUid.text.toString().uppercase()
            email = binding.inputEmail.text.toString().trim()
            password = binding.inputPassword.text.toString()

            //toast message if any one of the field is empty
            if (fullName.isEmpty() || uid.isEmpty() || email.isEmpty() || password.isEmpty())
                Toast.makeText(activity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            else {
                viewModel.getClientId(uid)
//                binding.progressBar.visibility = View.VISIBLE
//                registerUser(fullName, uid, email, password)
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
        password: String,
        clientId: String
    ) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    //once successfully authenticated, save user data to firestore
                    saveUserData(fullName, uid, clientId, currentUser.uid)
                }
            } else {
                Toast.makeText(
                    activity,
                    task.exception!!.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.deleteClient(clientId)
            }

        }
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    //saving user data to firestore
    private fun saveUserData(fullName: String, uid: String, clientId: String, currentUser: String) {
        val database = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["fullName"] = fullName+" "
        user["uid"] = uid
        user["client_id"] = clientId

        //name of collection on firestore is "Users", and the document will be mapped to firebase authentication uid
        database.collection("Users").document(currentUser).set(user).addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(activity, "Registered Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }.addOnFailureListener { e ->
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}