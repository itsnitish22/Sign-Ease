package com.teamdefine.signease.templates

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.api.modelsgetrequest.Templates
import com.teamdefine.signease.api.modelspostrequest.CustomFields
import com.teamdefine.signease.api.modelspostrequest.Document
import com.teamdefine.signease.api.modelspostrequest.Signers
import com.teamdefine.signease.api.modelspostrequest.SigningOptions
import com.teamdefine.signease.databinding.FragmentTemplateBinding
import java.util.*

class TemplateFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null //layout of recycler
    private var adapter: RecyclerView.Adapter<TemplateListAdapter.ViewHolder>? =
        null // recycler adapter
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var viewModel: TemplateListViewModel
    private val templateList: ArrayList<Pair<String, String>> = arrayListOf()
    private var templatePair: Pair<String, String> = Pair("", "")

    //just calendar things
    var dateSelectedByUser: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateBinding.inflate(inflater, container, false) //binding
        viewModel =
            ViewModelProvider(requireActivity())[TemplateListViewModel::class.java] //setting viewModel

        //getting templates and observing changes
        viewModel.getTemplates()
        viewModel.templates.observe(requireActivity(), Observer { template ->
            Log.i("Template Fragment", template.toString())
            addDataToArrayList(template)
        })

        return binding.root
    }

    //after getting templates, adding those to array list so as to send to recycler view
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addDataToArrayList(template: Templates?) {
        val templates = template?.templates
        if (templates != null) {
            for (i in templates) {
                templateList.add(Pair(i.template_id, i.title))
            }

            //sending array list to recycler view
            sendToRecyclerView()
        }
    }

    //sending array list with data to recyler view
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendToRecyclerView() {
        adapter = TemplateListAdapter(
            templateList,
            object : TemplateListAdapter.ItemClickListener {
                override fun onItemClick(template: Pair<String, String>) {
                    templatePair = template
                    //show calendar and get a date from user
                    Log.i("Template Frag", dateSelectedByUser)
                }
            })
//        if()
//        {
//            CoroutineScope(Dispatchers.IO).launch {
//                getCalendar(requireContext())
//                requestBody()
//            }
//        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    //get calendar function
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getCalendar(requireContext: Context): String {
        var date = ""
        val getDate = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext,
            R.style.Theme_Holo_Light_Dialog_NoActionBar,
            { datePicker, i, i2, i3 ->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, i)
                selectDate.set(Calendar.MONTH, i2)
                selectDate.set(Calendar.DAY_OF_MONTH, i3)
                dateSelectedByUser = formatDate.format(selectDate.time)
                Log.i("Template Frag", dateSelectedByUser)
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
        return date
    }

    fun requestBody() {
//        Creating the request body for Post request
        val template_ids = arrayListOf(templatePair.first)
        val subject = templatePair.second
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
        Log.i("helloabc123", document.toString())
    }
}