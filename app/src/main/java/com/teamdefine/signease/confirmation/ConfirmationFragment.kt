package com.teamdefine.signease.confirmation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.databinding.FragmentConfirmationBinding

class ConfirmationFragment : Fragment() {
    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding
    private val args: ConfirmationFragmentArgs by navArgs()
    private lateinit var requestBody: Document

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]

        //confirm button initially non clickable
        binding.confirmButton.isClickable = false
        binding.confirmButton.isEnabled = false

        //extracting args from navargs and setting it to requestBody
        requestBody = args.requestBody
        val name = requestBody.custom_fields[0].value
        val uid = requestBody.custom_fields[1].value
        val date = requestBody.custom_fields[2].value
        val templateTitle = requestBody.subject

        //displaying data
        displayData(date, name, uid, templateTitle)

        //if check box is checked, confirm button becomes clickable
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

        //click on change template, go to template frag
        binding.changeTemplateText.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToTemplateFragment())
            binding.progressBar.visibility = View.GONE
        }

        //click on change template, go to template frag
        binding.changeDateText.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToTemplateFragment())
            binding.progressBar.visibility = View.GONE
        }

        //on click of confirm  button
        binding.confirmButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.sendDocumentForSignature(requestBody) //sending request body to Post request
        }

        viewModel.check.observe(requireActivity()) { check ->
            if (check) {
                Handler().postDelayed({ //delay of 1 sec, server takes some time to update the total no of requests
                    binding.progressBar.visibility = View.GONE
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

    //displaying data function
    private fun displayData(date: String, name: String, uid: String, templateTitle: String) {
        binding.progressBar.visibility = View.VISIBLE

        binding.dateSelected.text = "Date selected: $date"
        binding.personNameText.text = name
        binding.userIdText.text = uid
        binding.templateSelectedText.text = templateTitle

        binding.progressBar.visibility = View.GONE
    }
}

