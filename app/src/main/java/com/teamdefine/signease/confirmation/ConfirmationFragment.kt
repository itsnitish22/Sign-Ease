package com.teamdefine.signease.confirmation

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

class ConfirmationFragment : Fragment() {

    private lateinit var viewModel: ConfirmationViewModel
    private lateinit var binding: FragmentConfirmationBinding
    var dateSelectedByUser: String = ""

//    @RequiresApi(Build.VERSION_CODES.N)
//    var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ConfirmationViewModel::class.java]

        binding.confirmButton.setOnClickListener {
            sendDocForSignatures()
        }
        return binding.root
    }

    private fun sendDocForSignatures() {
        Log.i("Confirmation Fragment", dateSelectedByUser)

//        Creating the request body for Post request
        val template_ids = arrayListOf("270b0c8e3d2cc376908d367b252151038b32719f")
        val subject = "Application For Duty Leave"
        val message = "Kindly review and approve my Duty Leave application."
        val tempSigners = Signers("HOD", "Aniket", "ani.khajanchi257@gmail.com")
        val signers = arrayListOf(tempSigners)
        val f1 = CustomFields("Full Name", "Nitish Sharma")
        val f2 = CustomFields("UID", "20BCS4122")
        val f3 = CustomFields("Date", dateSelectedByUser)
        val custom_fields = arrayListOf<CustomFields>(f1, f2, f3)
        val signing_options = SigningOptions(true, true, true, false, "draw")

        val document =
            Document(template_ids, subject, message, signers, custom_fields, signing_options, true)

        viewModel.sendDocumentForSignature(document)

    }
}
