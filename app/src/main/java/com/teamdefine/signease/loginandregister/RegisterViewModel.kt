package com.teamdefine.signease.loginandregister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.models.post_create_app.CreateAPIApp
import com.teamdefine.signease.api.models.post_create_app.response.CreateAppResponse
import kotlinx.coroutines.launch


class RegisterViewModel : ViewModel() {
    private val _appResponse: MutableLiveData<CreateAppResponse> = MutableLiveData()
    val appResponse: LiveData<CreateAppResponse>
        get() = _appResponse

    private val _deleteClient: MutableLiveData<Boolean?> = MutableLiveData(null)
    val deleteClient: LiveData<Boolean?>
        get() = _deleteClient

    private val _verifiedEmail: MutableLiveData<Boolean?> = MutableLiveData(null)
    val verifiedEmail: LiveData<Boolean?>
        get() = _verifiedEmail

    private val _clientError: MutableLiveData<Exception> = MutableLiveData()
    val clientError: LiveData<java.lang.Exception>
        get() = _clientError

    fun getClientId(uid: String) {
        val name = "Sign Ease${uid}"
        val domains = arrayListOf<String>()
        domains.add("example.com")
        val body = CreateAPIApp(name, domains)
        Log.i("helloabc", body.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createApp(body)
                Log.i("clientId", response.toString())
                _appResponse.value = response
            } catch (e: Exception) {
                _clientError.value = e
            }
        }
    }

    fun deleteClient(clientId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteApp(clientId)
                Log.i("success", response.toString())
                _deleteClient.value = response.code() == 204

            } catch (e: Exception) {
                _deleteClient.value = false
                Log.i("helloabc", e.toString())
            }
        }
    }

    fun verifyEmail(firebaseAuth: FirebaseAuth, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("RegisterVM Email", "Task Success")
                if (firebaseAuth.currentUser?.isEmailVerified == false) {
                    Log.i("RegisterVM Email", "Sending Email")
                    firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (task.isSuccessful)
                            Log.i("RegisterVM Email", "Sent Email")
                        else
                            Log.i("RegisterVM Email", "Sent Email Failed ${task.exception}")
                    }
                } else {
                    Log.e("RegisterVM Email", task.exception.toString())
                }
            }
        }
    }

    fun checkIsEmailVerified(
        firebaseAuth: FirebaseAuth,
        email: String,
        password: String
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.i("RegisterVM Email", "Inside Sign In")
            if (task.isSuccessful) {
                Log.i("RegisterVM Email", "Inside Sign In Success")
                if (firebaseAuth.currentUser?.isEmailVerified == true)
                    _verifiedEmail.setValue(true)
                else
                    _verifiedEmail.setValue(false)
            } else {
                Log.e("RegisterVM Email", "Inside Sign Failure")
                _verifiedEmail.setValue(false)
            }
        }
    }
}