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

    fun getClientId(uid: String) {
        val name = "Sign Ease${uid}"
        val domains = arrayListOf<String>()
        domains.add("example.com")
        val body = CreateAPIApp(name, domains)
        Log.i("helloabc", body.toString())
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createApp(body)
                _appResponse.value = response
            } catch (e: Exception) {
                Log.i("helloabc", e.toString())
            }
        }
    }
}