package com.teamdefine.signease.homepage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.domain.models.get_all_sign_requests.SignatureRequest
import com.teamdefine.domain.models.get_all_sign_requests.SignatureRequests
import com.teamdefine.signease.R
import com.teamdefine.signease.databinding.FragmentHomePageBinding
import com.teamdefine.signease.utils.Utility
import com.teamdefine.signease.utils.Utility.downloadFile
import org.koin.core.component.KoinComponent
import java.util.*

class HomePageFragment : Fragment(), KoinComponent {
    private lateinit var binding: FragmentHomePageBinding //binding
    private lateinit var firebaseAuth: FirebaseAuth //firebase auth
    private val viewModel: HomeFragmentViewModel by viewModels() //viewmodel
    private var adapter: RecyclerView.Adapter<HomePageAdapter.ViewHolder>? = null //adapter
    private lateinit var dialog: BottomSheetDialog //bottom sheet
    private val flag: HomePageFragmentArgs by navArgs()
    lateinit var currentUserDetail: MutableMap<String, Any> //users detail
    private var backPressedTime: Long = 0
    private var fileDownloadName: String? = null
    private var fileDownloadTitle: String? = null

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentHomePageBinding.inflate(inflater, container, false).also {
        binding = it //setting binding
        firebaseAuth = FirebaseAuth.getInstance() //firebase auth getting instance
        dialog = BottomSheetDialog(requireContext()) //bottom sheet
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackListener()
        initObservers()
        initClickListeners()

        //checking if user is logged in or not
        val loggedIn = checkUser(firebaseAuth)
        if (loggedIn) { //if logged in
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getSignatureRequests() // getting all signature requests made till date
            viewModel.getDataFromFirestore() // getting data from firestore
        } else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        if (flag.temp == 1) {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getSignatureRequests()
        }
    }

    private fun initBackListener() {
        //on back press clicked listener
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val toast =
                    Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT)
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    toast.show()
                    activity?.finish()
                } else
                    toast.show()
                backPressedTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun initObservers() {
        //observing changes from firestore to update the home page
        viewModel.data.observe(requireActivity()) { data ->
            val firstName = data["fullName"].toString().substringBefore(" ", "Not Found")
            currentUserDetail = data
            binding.welcomeText.text = "Welcome $firstName"
        }
        //observing changes in the received signature requests
        viewModel.requests.observe(requireActivity()) { requests ->
            loadDataInViews(requests)
        }

        //on receiving the download file url, using intent download the file
        viewModel.url.observe(requireActivity()) { url ->
            url?.let { fileUrl ->
                Log.i("HomePageFragment", "File URL: $fileUrl")
                fileDownloadName?.let {
                    downloadFile(fileUrl, fileDownloadTitle!!, fileDownloadName!!)
                    fileDownloadTitle = null
                    fileDownloadName = null
                }
            }
        }

        //observing changes from delete api request
        viewModel.check.observe(requireActivity()) { check ->
            if (check == true) {//if true, start refreshing and call the getSignatureRequests() to display updated data
                dialog.dismiss()
                binding.swipeRefresh.isRefreshing = true
                Handler().postDelayed({ //delay of 1 sec, server takes some time to update the total no of requests
                    viewModel.getSignatureRequests()
                }, 1000)
            }
        }
    }

    private fun loadDataInViews(requests: SignatureRequests) {
        val signatureRequest = arrayListOf<SignatureRequest>()

        //if already refreshing, stop it
        if (binding.swipeRefresh.isRefreshing)
            binding.swipeRefresh.isRefreshing = false

        val currentDate = Utility.getCurrentDate()

        val clientId = currentUserDetail["client_id"]
        for (i in requests.signature_requests) {
            if (i.client_id == clientId) {
                signatureRequest.add(i)
            }
        }

        binding.totalRequests.text = signatureRequest.size.toString()
        binding.lastUpdated.text = "Last Updated: $currentDate"

        //on getting data, show in recycler
        sendSignRequestsToRecycler(signatureRequest)
    }

    private fun initClickListeners() {
        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSignatureRequests() //calling getSignatureRequests() to update in views
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                HomePageFragmentDirections.actionHomePageFragmentToTemplateFragment()
            )
        }

        //log out button
        binding.logOut.setOnClickListener {
            val loggedIn = checkUser(firebaseAuth) //checks if the user if logged in
            if (loggedIn) { //if yes, log out
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("Do you really want to sign out?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            binding.progressBar.visibility = View.VISIBLE
            firebaseAuth.signOut()
            Toast.makeText(activity, "Signed out successfully", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
            val navigation = HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
            findNavController().navigate(navigation)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun sendSignRequestsToRecycler(signatureRequests: ArrayList<SignatureRequest>) {
        adapter = HomePageAdapter(signatureRequests, object : HomePageAdapter.ItemClickListener {
            override fun onItemClick(
                signature: SignatureRequest,
                position: Int,
                convertLongToTime: String
            ) { //getting the clicked item
                showBottomSheet(
                    signature,
                    convertLongToTime
                ) //show bottom sheet with delete and download button
            }
        })
        //recycler view stuff
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        //once data is shown in recycler view, disable prgress bar
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("ResourceAsColor")
    private fun showBottomSheet(signature: SignatureRequest, convertLongToTime: String) {
        //inflated bottom sheet
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

        //views, without binding
        val downloadButton = view.findViewById<Button>(R.id.downloadButton)
        val showNameText = view.findViewById<TextView>(R.id.subjectRequest)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        val deleteButton2 = view.findViewById<Button>(R.id.deleteButton2)

        //showing name of file, in bottom sheet
        showNameText.text = signature.subject

        //download button will download the file
        downloadButton.setOnClickListener {
            fileDownloadTitle = signature.subject
            fileDownloadName =
                "${signature.subject}_${convertLongToTime.filterNot { it.isWhitespace() }}.pdf"
            Log.i("HomePageFrag", "DownloadClick")
            viewModel.getFileUrl(signature.signature_request_id)
            dialog.dismiss()
        }

        //checking if signature request is completed or not
        if (!signature.is_complete) { //if completed, enable delete button (button with red color)
            deleteButton.setOnClickListener { //delete the request
                viewModel.deleteRequest(signature.signature_request_id)
            }
        } else {//otherwise disable red delete button and enable grey button
            deleteButton.visibility = View.GONE
            deleteButton2.visibility = View.VISIBLE
            deleteButton2.setOnClickListener {
                Toast.makeText(activity, "Request is signed", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setCancelable(true) //dialog can be dismissed upon swipe, back tap etc.
        dialog.setContentView(view)
        dialog.show()
    }

    //checking and returning if the user is logged in or not
    fun checkUser(firebaseAuth: FirebaseAuth): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}