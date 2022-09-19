package com.teamdefine.signease.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.modelsgetrequest.SignatureRequests
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    private val _requests: MutableLiveData<SignatureRequests> = MutableLiveData()
    val requests: LiveData<SignatureRequests>
        get() = _requests

    fun getSignatureRequests() {
        viewModelScope.launch {
            val signatureRequestsResponse = RetrofitInstance.api.getSignatureRequests()
            _requests.value = signatureRequestsResponse
        }
    }

}