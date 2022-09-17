package com.teamdefine.signease.confirmation

import android.R
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.teamdefine.signease.api.modelspostrequest.CustomFields
import com.teamdefine.signease.api.modelspostrequest.Document
import com.teamdefine.signease.api.modelspostrequest.Signers
import com.teamdefine.signease.api.modelspostrequest.SigningOptions
import com.teamdefine.signease.databinding.FragmentConfirmationBinding
import java.util.*

class ConfirmationFragment : Fragment() {
    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding
    var dateSelectedByUser: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]

        binding.selectDate.setOnClickListener {
            getCalendar()
        }

        binding.confirmButton.setOnClickListener {
            sendDocForSignatures()
        }

        return binding.root
    }

    private fun sendDocForSignatures() {
        Log.i("Confirmation Fragment", dateSelectedByUser)
        val template_ids = arrayListOf("270b0c8e3d2cc376908d367b252151038b32719f")
        val subject = "Application For Duty Leave"
        val message = "Kindly review and approve my Duty Leave application."
        val tempSigners = Signers("HOD", "Nitish", "nitish.sharma1186@gmail.com")
        val signers = arrayListOf(tempSigners)
        val f1 = CustomFields("Full Name", "Nitish Sharma")
        val f2 = CustomFields("UID", "20BCS4122")
        val f3 = CustomFields("Date", dateSelectedByUser)
//        val cc = CC("Father","ani.khajanchi257@gmail.com")
//        val ccs= arrayListOf(cc)
        val custom_fields = arrayListOf<CustomFields>(f1, f2, f3)
        val signing_options = SigningOptions(true, true, true, false, "draw")

        val document =
            Document(template_ids, subject, message, signers, custom_fields, signing_options, true)

        viewModel.sendDocumentForSignature(document)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCalendar() {
        var date = ""
        val getDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.Theme_Holo_Light_Dialog_NoActionBar,
            { datePicker, i, i2, i3 ->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, i)
                selectDate.set(Calendar.MONTH, i2)
                selectDate.set(Calendar.DAY_OF_MONTH, i3)
                date = formatDate.format(selectDate.time)
                binding.date.text = date
                dateSelectedByUser = date
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
}