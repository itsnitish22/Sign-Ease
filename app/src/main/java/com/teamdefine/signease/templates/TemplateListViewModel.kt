package com.teamdefine.signease.templates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import com.teamdefine.signease.api.modelsgetrequest.Templates
import kotlinx.coroutines.launch

class TemplateListViewModel : ViewModel() {
    private val _templates: MutableLiveData<Templates> = MutableLiveData()
    val templates: LiveData<Templates>
        get() = _templates

    fun getTemplates() {
        viewModelScope.launch {
            val templatesResponse = RetrofitInstance.api.getTemplates()
            Log.i("Template List VM", templatesResponse.toString())
            _templates.value = templatesResponse
        }
    }
}