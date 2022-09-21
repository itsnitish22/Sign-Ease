package com.teamdefine.signease.templates

import android.R
import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.api.models.get_all_templates.Template
import com.teamdefine.signease.api.models.get_all_templates.Templates
import com.teamdefine.signease.api.models.post_template_for_sign.CustomFields
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.api.models.post_template_for_sign.Signers
import com.teamdefine.signease.api.models.post_template_for_sign.SigningOptions
import com.teamdefine.signease.databinding.FragmentTemplateBinding
import java.util.*

class TemplateFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null //layout of recycler
    private var adapter: RecyclerView.Adapter<TemplateListAdapter.ViewHolder>? =
        null // recycler adapter
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var viewModel: TemplateListViewModel
    private val templateList: ArrayList<Template> =
        arrayListOf()   //List of Templates to pass in the recycler view
    private var templateSelected: Template =
        Template("", "", 0)   //Empty template created to further store the clicked template
    private lateinit var currentUserDetail: MutableMap<String, Any>

    //will be initialized when calendar returns the date on selection
    var dateSelectedByUser: String = ""

    var formatDate = SimpleDateFormat("dd MMMM YYYY", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateBinding.inflate(inflater, container, false) //binding
        viewModel =
            ViewModelProvider(requireActivity())[TemplateListViewModel::class.java] //setting viewModel
        viewModel.getTemplates()    //getting templates and observing changes
        viewModel.templates.observe(requireActivity()) { template ->
            Log.i("Template Fragment", template.toString())
            addDataToArrayList(template)
        }

        viewModel.getDataFromFirestore()    //getting current user data from Firestore
        viewModel.data.observe(requireActivity(), Observer { data ->
            currentUserDetail = data
            Log.i("helloabc89", data.toString())
        })

        return binding.root
    }

    //after getting templates, adding those to array list so as to send to recycler view
    private fun addDataToArrayList(template: Templates?) {
        val templates = template?.templates
        if (templates != null) {
            for (i in templates) {
                val t = Template(i.template_id, i.title, i.updated_at)
                templateList.add(t)
            }
            sendToRecyclerView()        //sending array list to recycler view
        }
    }

    //sending array list with data to recycler view
    private fun sendToRecyclerView() {
        adapter = TemplateListAdapter(
            templateList,
            object : TemplateListAdapter.ItemClickListener {
                override fun onItemClick(template: Template) {
                    templateSelected = template //initializing template details which is clicked
                    getCalendar(requireContext())   //show calendar and get a date from user
                }
            })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    fun getCalendar(requireContext: Context): String {    //get calendar function to pick a date
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
                requestBody()   //calling requestBody() to generate the body after getting the date
            },
            getDate.get(Calendar.YEAR),
            getDate.get(Calendar.MONTH),
            getDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
        return date
    }

    private fun requestBody() {     //Creating the request body for Post request

        val template_ids = arrayListOf(templateSelected.template_id)
        val subject = templateSelected.title
        val message = "Kindly review and approve my Duty Leave application."
        val tempSigners = Signers("HOD", "Aniket", "ani.khajanchi257@gmail.com")
        val signers = arrayListOf(tempSigners)
        val f1 = CustomFields("Full Name", "${currentUserDetail.getValue("fullName")}")
        val f2 = CustomFields("UID", "${currentUserDetail.getValue("uid")}")
        val f3 = CustomFields("Date", dateSelectedByUser)
        val customFields = arrayListOf<CustomFields>(f1, f2, f3)
        val signingOptions = SigningOptions(true, true, true, false, "draw")

        val document =
            Document(template_ids, subject, message, signers, customFields, signingOptions, true)
        Log.i("helloabc123", document.toString())

        findNavController().navigate(   //Navigating to Confirmation Fragment with the request body for Post Request
            TemplateFragmentDirections.actionTemplateFragmentToConfirmationFragment(
                document
            )
        )
    }
}