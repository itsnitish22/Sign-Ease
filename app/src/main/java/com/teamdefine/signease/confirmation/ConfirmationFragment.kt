package com.teamdefine.signease.confirmation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.databinding.FragmentConfirmationBinding

class ConfirmationFragment : Fragment() {

    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding
    private val flag: ConfirmationFragmentArgs by navArgs()
    private lateinit var requestBody: Document

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]
        requestBody = flag.requestBody
        Log.i("helloabc56", requestBody.toString())
        binding.confirmButton.setOnClickListener {
            viewModel.sendDocumentForSignature(requestBody) //sending request body to Post request
        }

        viewModel.check.observe(requireActivity()){check->
            if(check){
                Handler().postDelayed({ //delay of 1 sec, server takes some time to update the total no of requests
                    findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToHomePageFragment(1))
                }, 5000)
            }
        }
        return binding.root
    }
}
