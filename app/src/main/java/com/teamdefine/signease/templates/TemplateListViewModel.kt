package com.teamdefine.signease.templates

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamdefine.signease.api.RetrofitInstance
import kotlinx.coroutines.launch

class TemplateListViewModel: ViewModel() {
    fun getTemplates(){
        viewModelScope.launch {
            val templatesResponse = RetrofitInstance.api.getTemplates()
            Log.i("Template List VM", templatesResponse.toString())
        }
    }
}