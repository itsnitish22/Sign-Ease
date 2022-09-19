package com.teamdefine.signease.templates

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.models.get_all_templates.Templates
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val _templates: MutableLiveData<Templates> = MutableLiveData()
    val templates: LiveData<Templates>
        get() = _templates
    private lateinit var firebaseAuth: FirebaseAuth
    var firebaseData = mutableMapOf<String, Any>() //global variable which will store user data
    private val _data:MutableLiveData<MutableMap<String,Any>> = MutableLiveData()
    val data:LiveData<MutableMap<String,Any>>
        get()=_data

    fun getTemplates() {
        viewModelScope.launch {
            val templatesResponse = RetrofitInstance.api.getTemplates()
            Log.i("Template List VM", templatesResponse.toString())
            _templates.value = templatesResponse
        }
    }
    //getting current user data from firestore
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
                            _data.value=firebaseData
                            Log.i("HomePageFragment", "DocumentSnapshot: $firebaseData")
                        }
                    }
            }
        }
    }
}