package com.teamdefine.signease.confirmation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.modelspostrequest.Document
import kotlinx.coroutines.launch

class ConfirmationViewModel : ViewModel() {
//    private val _sentConfirm: MutableLiveData<String> = MutableLiveData()
//    val sentConfirm: LiveData<String>
//        get() = _sentConfirm

    fun sendDocumentForSignature(document: Document) {
        viewModelScope.launch {
            try {
//                RetrofitInstance.api.sendDocForSignatures(document)
                Log.i("Confirmation View Model", "Done")
            } catch (e: Exception) {
                Log.i("Confirmation View Model", e.toString())
            }
        }
    }
}