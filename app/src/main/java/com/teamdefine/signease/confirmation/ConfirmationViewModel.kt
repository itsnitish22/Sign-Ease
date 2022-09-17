package com.teamdefine.signease.confirmation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.modelspostrequest.Document
import kotlinx.coroutines.launch

class ConfirmationViewModel : ViewModel() {
//    private val _sentConfirm: MutableLiveData<String> = MutableLiveData()
//    val sentConfirm: LiveData<String>
//        get() = _sentConfirm

    @SuppressLint("LongLogTag")
    fun sendDocumentForSignature(document: Document) {
        viewModelScope.launch {
            try {
                Log.i("Confirmation View Model 1", document.toString())
                val response = RetrofitInstance.api.sendDocForSignatures(document)
                Log.i("Confirmation View Model 2", "Done")
                Log.i("Confirmation View Model 3", response.toString())
            } catch (e: Exception) {
                Log.i("Confirmation View Model 4", e.toString())
            }
        }
    }
}