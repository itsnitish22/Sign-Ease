package com.teamdefine.signease.templates

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.teamdefine.signease.DatePicker
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.get_all_templates.Template
import com.teamdefine.signease.api.models.get_all_templates.Templates
import com.teamdefine.signease.databinding.FragmentTemplateBinding
import java.text.SimpleDateFormat
import java.util.*

class TemplateFragment : Fragment() {
    private var adapter: RecyclerView.Adapter<TemplateListAdapter.ViewHolder>? =
        null // recycler adapter
    private lateinit var binding: FragmentTemplateBinding //binding
    private lateinit var viewModel: TemplateListViewModel //viewmodel
    private val templateList: ArrayList<Template> =
        arrayListOf()   //List of Templates to pass in the recycler view
    lateinit var templateSelected: Template  //Empty template created to further store the clicked template
    lateinit var currentUserDetail: MutableMap<String, Any> //users detail
    private lateinit var dialog: BottomSheetDialog //bottom sheet
    private lateinit var bottomView: View

    //will be initialized when calendar returns the date on selection
    var dateSelectedByUser: String = "" //date selected by user, initially empty
    var endDateSelected: String = ""
    var dateSelectedLong: Long = 0
    var endDateSelectedLong: Long = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateBinding.inflate(inflater, container, false) //binding
        viewModel =
            ViewModelProvider(requireActivity())[TemplateListViewModel::class.java] //setting viewModel
        binding.progressBar.visibility =
            View.VISIBLE //on initial opening of screen, show progress bar
        bottomView =
            layoutInflater.inflate(com.teamdefine.signease.R.layout.template_bottom_sheet, null)
        dialog = BottomSheetDialog(requireContext()) //bottom sheet

        binding.floatingActionButtonHome.setOnClickListener {
            findNavController().navigate(TemplateFragmentDirections.actionTemplateFragmentToHomePageFragment())
        }

        viewModel.getTemplates()    //getting templates and observing changes
        viewModel.templates.observe(requireActivity()) { template ->
            //if already refreshing, stop it
            if (binding.swipeRefresh.isRefreshing)
                binding.swipeRefresh.isRefreshing = false

            Log.i("Template Fragment", template.toString())
            addDataToArrayList(template) //adding the data to array list
        }

        //getting current user data from firestore
        viewModel.getDataFromFirestore()
        viewModel.data.observe(requireActivity()) { data ->
            currentUserDetail = data //setting global variables
        }

        //observing, if the templates are received or not
        viewModel.completed.observe(requireActivity()) { completed ->
            if (completed) //if completed, hide progress bar
                binding.progressBar.visibility = View.GONE
        }

        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getTemplates() //calling getTemplates() to update in views
        }

        //on back press clicked listener
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(TemplateFragmentDirections.actionTemplateFragmentToHomePageFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }

    //after getting templates, adding those to array list so as to send to recycler view
    private fun addDataToArrayList(template: Templates?) {
        templateList.clear()
        val templates = template?.templates
        if (templates != null) {
            for (i in templates) {
                val t = Template(i.template_id, i.title, i.updated_at, i.signer_roles)
                templateList.add(t)
            }
            sendToRecyclerView() //sending array list to recycler view
        }
    }

    //sending array list with data to recycler view
    private fun sendToRecyclerView() {
        adapter = TemplateListAdapter(
            templateList,
            object : TemplateListAdapter.ItemClickListener {
                override fun onItemClick(template: Template) {
                    templateSelected = template //initializing template details which is clicked
                    if (templateSelected.title == "Night-Pass")
                        getRangeCalendar()
                    else
                        getCalendar()
                }
            },
            object : TemplateListAdapter.ItemEyeClickListener {
                override fun onItemEyeClickListener(template: Template) {
                    showBottomSheet(template)
                }
            })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    //getting the calendar for date selection
    fun getCalendar() {
        val datePicker: MaterialDatePicker<Long> =
            DatePicker().getCalendar(Date().time)
        //show calendar and get a date from user
        datePicker.show(requireFragmentManager(), "tag")
        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            dateSelectedByUser = simpleDateFormat.format(Date(it))
            dateSelectedLong = it
            if (templateSelected.title == "Application For Duty Leave")
                requestBody("")
            else {
                showDialog()
            }
        }
    }

    fun getRangeCalendar() {
        val datePicker = DatePicker().getCalendar2(Date().time, Date().time)
        //show calendar and get a date from user
        datePicker.show(requireFragmentManager(), "tag")
        datePicker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            dateSelectedByUser = simpleDateFormat.format(Date(it.first))
            dateSelectedLong = it.first
            endDateSelected = simpleDateFormat.format(Date(it.second))
            endDateSelectedLong = it.second
            if (templateSelected.title == "Application For Duty Leave")
                requestBody("")
            else {
                showDialog()
            }
        }
    }

    //custom dialog, don't ask me why!
    private fun showDialog() {
        val reasonDialogView = LayoutInflater.from(activity)
            .inflate(R.layout.reason_dialog, null)
        val dialogBuilder =
            AlertDialog.Builder(activity).setView(reasonDialogView).setTitle("Reason for leave")
        val alertDialog = dialogBuilder.show()

        val confirmButton = reasonDialogView.findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            alertDialog.dismiss()
            var reason = ""
            reason = reasonDialogView.findViewById<EditText>(R.id.input_reason).text.toString()
            requestBody(reason)   //calling requestBody() to generate the body after getting the date
        }
        val cancelButton = reasonDialogView.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    //creating the request body for post request
    private fun requestBody(reason: String) {
        var reasons = ""
        when (templateSelected.title) {
            "Day-Pass" -> {
                reasons = reason
            }
            "Night-Pass" -> {
                reasons = reason
            }
        }
        val document = RequestBody().getRequestBody(
            reasons,
            templateSelected,
            currentUserDetail,
            dateSelectedByUser,
            endDateSelected
        )

        //navigating to Confirmation Fragment with the request body for Post Request
        findNavController().navigate(
            TemplateFragmentDirections.actionTemplateFragmentToConfirmationFragment(
                document, dateSelectedLong, endDateSelectedLong
            )
        )
        activity?.fragmentManager?.popBackStack();
    }

    @SuppressLint("ResourceAsColor")
    //show bottom sheet
    private fun showBottomSheet(template: Template) {
        binding.progressBar2.visibility = View.VISIBLE
        val pdf = URLs.getUrlsFromData()[template.title]
        val webView = bottomView.findViewById<WebView>(com.teamdefine.signease.R.id.webView2)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
        bottomView.findViewById<TextView>(com.teamdefine.signease.R.id.subjectTemplate).text =
            template.title

        //web view shown by using onPageFinished function, this helps us by pass the handler post delayed solution
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar2.visibility = View.GONE
                dialog.setContentView(bottomView)
                dialog.show()
                dialog.setCancelable(true)
            }
        }

    }
}
