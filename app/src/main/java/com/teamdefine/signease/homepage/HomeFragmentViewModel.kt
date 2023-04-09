package com.teamdefine.signease.homepage

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.models.get_all_sign_requests.SignatureRequests
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    var firebaseData = mutableMapOf<String, Any>()

    private val _requests: MutableLiveData<SignatureRequests> = MutableLiveData()
    val requests: LiveData<SignatureRequests>
        get() = _requests

    private val _data: MutableLiveData<MutableMap<String, Any>> = MutableLiveData()
    val data: LiveData<MutableMap<String, Any>>
        get() = _data

    private val _url: MutableLiveData<String?> = MutableLiveData(null)
    val url: LiveData<String?>
        get() = _url

    private val _check: MutableLiveData<Boolean> = MutableLiveData(false)
    val check: LiveData<Boolean>
        get() = _check

    //getting signature requests
    fun getSignatureRequests() {
        viewModelScope.launch {
            try {
                val signatureRequestsResponse = RetrofitInstance.api.getSignatureRequests()
                _requests.value = signatureRequestsResponse
            } catch (e: Exception) {
                Log.i("HomePage VM", e.toString())
            }
        }
    }

    //getting data from firestore
    @SuppressLint("LongLogTag")
    fun getDataFromFirestore() {
        viewModelScope.launch {
            firebaseAuth = FirebaseAuth.getInstance()
            val database = FirebaseFirestore.getInstance()
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                database.collection("Users").document(firebaseUser.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            firebaseData =
                                document.data as MutableMap<String, Any> //setting data to global variable
                            _data.value = firebaseData
                            Log.i("HomePageFragment VM", "DocumentSnapshot: $firebaseData")
                        }
                    }
            }
        }
    }

    //getting public download link of file
    fun getFileUrl(sign_id: String) {
        viewModelScope.launch {
            try {
                val fileUrl = RetrofitInstance.api.getURL(sign_id, true)
                _url.postValue(fileUrl.file_url)
            } catch (e: Exception) {
                Log.e("HomePage VM", e.toString())
            }
        }
    }

    //delete request
    fun deleteRequest(sign_id: String) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.deleteRequest(sign_id)
                _check.value = true
            } catch (e: Exception) {
                Log.i("HomePage VM", e.toString())
            }
        }
    }
}