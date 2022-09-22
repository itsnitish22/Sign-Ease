package com.teamdefine.signease.confirmation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.databinding.FragmentConfirmationBinding
import com.teamdefine.signease.templates.URLs


class ConfirmationFragment : Fragment() {

    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding

        private val flag: ConfirmationFragmentArgs by navArgs()
    private lateinit var requestBody: Document

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]

        requestBody = flag.requestBody
        Log.i("helloabc56", requestBody.toString())
        val name=requestBody.custom_fields[0].value
        val uid=requestBody.custom_fields[1].value
        val date=requestBody.custom_fields[2].value
        val template_title=requestBody.subject
        val message=requestBody.message
        val signers=requestBody.signers

        //web view stuff
//        val webview = binding.webView
//        webview.settings.javaScriptEnabled = true
//        val pdf =
//            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/DL.pdf?alt=media&token=863e24b4-fd59-496c-ab63-7e3fb78a6476"
//        webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
//        val pdf2 = URLs.getUrlsFromData()["Application For Duty Leave"]
//        Log.i("Confirm Frag LULLA", pdf2.toString())




        binding.confirmButton.setOnClickListener {
            viewModel.sendDocumentForSignature(requestBody) //sending request body to Post request
        }

        viewModel.check.observe(requireActivity()) { check ->
            if (check) {
                Handler().postDelayed({ //delay of 1 sec, server takes some time to update the total no of requests
                    findNavController().navigate(
                        ConfirmationFragmentDirections.actionConfirmationFragmentToHomePageFragment(
                            1
                        )
                    )
                }, 5000)
            }
        }
        return binding.root
    }
}


//    private fun displayData() {
//        binding.date.text=requestBody.custom_fields[2].value
//        binding.userName.text=requestBody.custom_fields[0].value
//        binding.userUID.text=requestBody.custom_fields[1].value
//        binding.templateSelected.text=requestBody.subject
//    }

