package com.teamdefine.signease.loginandregister

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.models.post_create_app.CreateAPIApp
import com.teamdefine.signease.api.models.post_create_app.response.CreateAppResponse
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _appResponse: MutableLiveData<CreateAppResponse> = MutableLiveData()
    val appResponse: LiveData<CreateAppResponse>
        get() = _appResponse

    private val _deleteClient: MutableLiveData<Boolean> = MutableLiveData(false)
    val deleteClient: LiveData<Boolean>
        get() = _deleteClient

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
                Log.i("helloabc", e.toString())
            }
        }
    }

    fun deleteClient(clientId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteApp(clientId)
                Log.i("success", response.toString())
                if (response.code() == 204)
                    _deleteClient.value = true

            } catch (e: Exception) {
                Log.i("helloabc", e.toString())
            }
        }
    }
}