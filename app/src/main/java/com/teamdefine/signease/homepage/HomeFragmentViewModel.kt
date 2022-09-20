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
    var firebaseData = mutableMapOf<String, Any>() //global variable which will store user data

    private val _requests: MutableLiveData<SignatureRequests> = MutableLiveData()
    val requests: LiveData<SignatureRequests>
        get() = _requests

    private val _data: MutableLiveData<MutableMap<String, Any>> = MutableLiveData()
    val data: LiveData<MutableMap<String, Any>>
        get() = _data

    private val _url:MutableLiveData<String> = MutableLiveData()
    val url:LiveData<String>
        get() = _url

    fun getSignatureRequests() {
        viewModelScope.launch {
            val signatureRequestsResponse = RetrofitInstance.api.getSignatureRequests()
            _requests.value = signatureRequestsResponse
        }
    }

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
    fun getFileUrl(sign_id:String){
        viewModelScope.launch {
            val fileUrl=RetrofitInstance.api.getURL(sign_id,true)
            _url.value=fileUrl.file_url
        }
    }
}