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
import com.teamdefine.signease.databinding.FragmentConfirmationBinding
import java.util.*

class ConfirmationFragment : Fragment() {
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