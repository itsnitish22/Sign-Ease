package com.teamdefine.signease.confirmation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.api.models.post_template_for_sign.response.ResponseSign
import kotlinx.coroutines.launch

class ConfirmationViewModel : ViewModel() {
    private val _responses: MutableLiveData<ResponseSign> = MutableLiveData()
    val responses: LiveData<ResponseSign>
        get() = _responses
    var check:Boolean?=null
    //function to post doc for signs
    @SuppressLint("LongLogTag")
    fun sendDocumentForSignature(document: Document) {
        viewModelScope.launch {
            try {
                Log.i("Confirmation View Model 1", document.toString())
                val response = RetrofitInstance.api.sendDocForSignatures(document)
                check=true
                _responses.value=response
                Log.i("Confirmation View Model 2", "Done")
                Log.i("Confirmation View Model 3", response.toString())
            } catch (e: Exception) {
                Log.i("Confirmation View Model 4", e.toString())
            }
        }
    }
}