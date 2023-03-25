package com.teamdefine.signease.loginandregister

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.teamdefine.signease.R
import com.teamdefine.signease.databinding.FragmentRegisterBinding
import com.teamdefine.signease.utils.Utility.toast

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding //binding
    private lateinit var auth: FirebaseAuth //firebase auth
    private val viewModel: RegisterViewModel by viewModels()
    private var clientId: String = ""
    private var fullName: String = ""
    private var uid: String = ""
    private var email: String = ""
    private var password: String = ""
    var checkAction = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentRegisterBinding.inflate(inflater, container, false).also {
        binding = it
        auth = FirebaseAuth.getInstance()
        initObservers()
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        checkAction = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkAction = false
    }

    override fun onResume() {
        super.onResume()
        checkAction = false
    }

    private fun initObservers() {
        viewModel.appResponse.observe(requireActivity()) { response ->
            if (checkAction) {
                clientId = response.api_app.client_id
                binding.progressBar.visibility = View.VISIBLE
                saveUserData(
                    fullName,
                    uid,
                    clientId,
                    auth.currentUser?.uid.toString(),
                    email,
                    password
                )
            }
        }
        viewModel.deleteClient.observe(requireActivity()) { deleteClient ->
            deleteClient?.let {
                if (it) {
                    binding.progressBar.visibility = View.GONE
                    binding.inputPassword.setText("")
                    binding.inputPassword.requestFocus()
                    view?.showKeyboard()
                }
            }
        }
        viewModel.clientError.observe(requireActivity()) {
            toast(it.toString())
            binding.progressBar.visibility = View.GONE
            binding.inputUid.setText("")
            binding.inputUid.requestFocus()
            changeVerifyEmailButtonAppearanceToEnable()
            view?.showKeyboard()
        }
        viewModel.verifiedEmail.observe(requireActivity(), Observer { verfiedEmail ->
            verfiedEmail?.let {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                    checkAction = true
                    viewModel.getClientId(uid)
                } else if (!it) {
                    try {
                        toast("Your email is not verified. First verify it")
                    } catch (e: Exception) {
                        Log.e("RegisterFragment Toast Ex:", e.toString())
                    }
                }
            }
        })
    }

    private fun initClickListeners() {
        binding.signUpButton.setOnClickListener {
            fullName = binding.inputName.text.toString()
            uid = binding.inputUid.text.toString().uppercase()
            email = binding.inputEmail.text.toString().trim()
            password = binding.inputPassword.text.toString()

            if (fullName.isEmpty() || uid.isEmpty() || email.isEmpty() || password.isEmpty())
                toast("All fields are mandatory")
            else {
                checkIsEmailVerified(email, password)
            }
        }
        binding.verifyEmail.setOnClickListener {
            email = binding.inputEmail.text.toString().trim()
            password = binding.inputPassword.text.toString()
            if (email.isEmpty() || password.isEmpty())
                toast("Enter email and password first")
            else {
                checkAction = true
                changeVerifyEmailButtonAppearanceToDisable()
                verifyEmail(email, password)
            }
        }
        binding.signIn.setOnClickListener {
            checkAction = false
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun checkIsEmailVerified(email: String, password: String) {
        viewModel.checkIsEmailVerified(auth, email, password)
    }

    private fun verifyEmail(email: String, password: String) {
        viewModel.verifyEmail(auth, email, password)
    }

    private fun changeVerifyEmailButtonAppearanceToDisable() {
        binding.verifyEmail.isClickable = false
        binding.verifyEmail.isEnabled = false
        binding.verifyEmail.setBackgroundColor(
            binding.verifyEmail.context.resources.getColor(
                R.color.grey
            )
        )
    }

    private fun changeVerifyEmailButtonAppearanceToEnable() {
        binding.verifyEmail.isClickable = true
        binding.verifyEmail.isEnabled = true
        binding.verifyEmail.setBackgroundColor(
            binding.verifyEmail.context.resources.getColor(
                R.color.blue
            )
        )
    }

    //register the user on firebase (authenticate)
    private fun registerUser(
        fullName: String,
        uid: String,
        email: String,
        password: String,
        clientId: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    //once successfully authenticated, save user data to firestore
//                    saveUserData(fullName, uid, clientId, currentUser.uid)
                }
            } else {
                toast(task.exception!!.message.toString())
                viewModel.deleteClient(clientId)
            }

        }
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    //saving user data to firestore
    private fun saveUserData(
        fullName: String,
        uid: String,
        clientId: String,
        currentUser: String,
        email: String,
        password: String
    ) {
        val database = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["fullName"] = fullName + " "
        user["uid"] = uid
        user["client_id"] = clientId
        user["email"] = email
        user["password"] = password

        //name of collection on firestore is "Users", and the document will be mapped to firebase authentication uid
        database.collection("Users").document(currentUser).set(user).addOnSuccessListener {
            binding.progressBar.visibility = View.GONE
            toast("Registered Successfully")
            checkAction = false
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomePageFragment())
        }.addOnFailureListener { e ->
            toast(e.toString())
        }
    }
}