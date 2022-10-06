package com.teamdefine.signease.confirmation

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamdefine.signease.DatePicker
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.databinding.FragmentConfirmationBinding
import java.util.*

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
        var dateEnd = ""
        val templateTitle = requestBody.subject
        if (templateTitle == "Night-Pass")
            dateEnd = requestBody.custom_fields[3].value


        //displaying data
        displayData(date, name, uid, templateTitle, dateEnd)

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
            if (templateTitle == "Night-Pass")
                getRangeCalendar()
            else
                getCalendar()
        }

        //on click of confirm  button
        binding.confirmButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.sendDocumentForSignature(requestBody) //sending request body to Post request
        }

        viewModel.responses.observe(viewLifecycleOwner) { response ->
            Log.i("helloabc", response.toString())
            if (viewModel.check != null) {
                Handler().postDelayed({ //delay of 3 sec, server takes some time to update the total no of requests
                    binding.progressBar.visibility = View.GONE
                    viewModel.check = null
                    view?.post {
                        findNavController().navigate(
                            ConfirmationFragmentDirections.actionConfirmationFragmentToHomePageFragment(
                                1
                            )
                        )
                    }
                }, 5000)
            }
        }

        //on back press clicked listener
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToTemplateFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

//    override fun onDestroyView() {
//        viewModel.check?.removeObservers(this)
//        super.onDestroyView()
//
//    }

    private fun getRangeCalendar() {
        val datePicker = DatePicker().getCalendar2(args.dateSelected, args.endDateSelected)
        datePicker.show(requireFragmentManager(), "tag")
        datePicker.addOnPositiveButtonClickListener {
            Log.i("helloabc", it.toString())
            val newDateSelected =
                SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(it.first))
            val newEndDateSelected =
                SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(it.second))
            args.requestBody.custom_fields[2].value = newDateSelected
            args.requestBody.custom_fields[3].value = newEndDateSelected
            binding.dateSelected.text = "$newDateSelected - $newEndDateSelected"
        }
    }

    //displaying data function
    private fun displayData(
        date: String,
        name: String,
        uid: String,
        templateTitle: String,
        dateEnd: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        if (templateTitle == "Night-Pass")
            binding.dateSelected.text = "$date - $dateEnd"
        else
            binding.dateSelected.text = "$date"
        binding.personNameText.text = name
        binding.userIdText.text = uid
        binding.templateSelectedText.text = templateTitle

        binding.progressBar.visibility = View.GONE
    }

    private fun getCalendar() {
        val datePicker =
            DatePicker().getCalendar(args.dateSelected) //show calendar and get a date from user
        datePicker.show(requireFragmentManager(), "tag")
        datePicker.addOnPositiveButtonClickListener {
            Log.i("helloabc", it.toString())
            val newDateSelected = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(Date(it))
            args.requestBody.custom_fields[2].value = newDateSelected
            binding.dateSelected.text = "$newDateSelected"
        }
    }
}

