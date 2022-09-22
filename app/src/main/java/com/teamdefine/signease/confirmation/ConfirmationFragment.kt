package com.teamdefine.signease.confirmation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.databinding.FragmentConfirmationBinding


class ConfirmationFragment : Fragment() {
    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding

    //    private val flag: ConfirmationFragmentArgs by navArgs()
    private lateinit var requestBody: Document

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]

//        requestBody = flag.requestBody
//        Log.i("helloabc56", requestBody.toString())


        //web view stuff
//        val webview = binding.webView
//        webview.settings.javaScriptEnabled = true
//        val pdf =
//            "https://firebasestorage.googleapis.com/v0/b/sign-ease.appspot.com/o/DL.pdf?alt=media&token=863e24b4-fd59-496c-ab63-7e3fb78a6476"
//        webview.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
//        val pdf2 = URLs.getUrlsFromData()["Application For Duty Leave"]
//        Log.i("Confirm Frag LULLA", pdf2.toString())

        binding.confirmButton.isClickable = false
        binding.confirmButton.isEnabled = false
        binding.checkBox.setOnClickListener {
            if (binding.checkBox.isChecked) {
                binding.confirmButton.isClickable = true
                binding.confirmButton.isEnabled = true
                binding.confirmButton.setBackgroundColor(
                    binding.confirmButton.context.resources.getColor(
                        R.color.blue
                    )
                )
            } else {
                binding.confirmButton.isClickable = false
                binding.confirmButton.isEnabled = false
                binding.confirmButton.setBackgroundColor(
                    binding.confirmButton.context.resources.getColor(
                        R.color.grey
                    )
                )
            }
        }

        binding.changeTemplateText.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToTemplateFragment())
            binding.progressBar.visibility = View.GONE
        }

        binding.changeDateText.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToTemplateFragment())
            binding.progressBar.visibility = View.GONE
        }

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

