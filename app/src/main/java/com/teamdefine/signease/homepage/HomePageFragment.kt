package com.teamdefine.signease.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.get_all_sign_requests.SignatureRequest
import com.teamdefine.signease.databinding.FragmentHomePageBinding
import java.text.SimpleDateFormat
import java.util.*

class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding //binding
    private lateinit var firebaseAuth: FirebaseAuth //firebase auth
    private lateinit var viewModel: HomeFragmentViewModel //viewmodel
    private var adapter: RecyclerView.Adapter<HomePageAdapter.ViewHolder>? = null //adapter
    private lateinit var dialog: BottomSheetDialog //bottom sheet
    private val flag: HomePageFragmentArgs by navArgs()

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false) //setting binding
        viewModel =
            ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java] //setting viewmodel
        firebaseAuth = FirebaseAuth.getInstance() //firebase auth getting instance
        dialog = BottomSheetDialog(requireContext()) //bottom sheet

        //checking if user is logged in or not
        val loggedIn = checkUser(firebaseAuth)
        if (loggedIn) { //if logged in
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getSignatureRequests() // getting all signature requests made till date
            viewModel.getDataFromFirestore() // getting data from firestore
        } else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        //log out button
        binding.logOut.setOnClickListener {
            val loggedIn = checkUser(firebaseAuth) //checks if the user if logged in
            if (loggedIn) { //if yes, log out
                binding.progressBar.visibility = View.VISIBLE
                firebaseAuth.signOut()
                binding.progressBar.visibility = View.GONE
                val navigation = HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
                findNavController().navigate(navigation)
            }
        }

        //observing changes in the received signature requests
        viewModel.requests.observe(requireActivity()) { requests ->
            //if already refreshing, stop it
            if (binding.swipeRefresh.isRefreshing)
                binding.swipeRefresh.isRefreshing = false

            //formatting date
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            //setting views with data
            binding.totalRequests.text = requests.list_info.num_results.toString()
            binding.lastUpdated.text = "Last Updated: $currentDate"

            //on getting data, show in recycler
            sendSignRequestsToRecycler(requests.signature_requests)
        }

        //observing changes from firestore to update the home page
        viewModel.data.observe(requireActivity()) { data ->
            val firstName = data["fullName"].toString().substringBefore(" ", "Not Found")
            binding.welcomeText.text = "Welcome $firstName"
        }

        //on receiving the download file url, using intent download the file
        viewModel.url.observe(requireActivity()) { url ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
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

        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSignatureRequests() //calling getSignatureRequests() to update in views
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(
                HomePageFragmentDirections.actionHomePageFragmentToTemplateFragment()
            )
        }
        if (flag.temp == 1) {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getSignatureRequests()
        }

        return binding.root
    }

    private fun sendSignRequestsToRecycler(signatureRequests: ArrayList<SignatureRequest>) {
        adapter = HomePageAdapter(signatureRequests, object : HomePageAdapter.ItemClickListener {
            override fun onItemClick(
                signature: SignatureRequest,
                position: Int
            ) { //getting the clicked item
                showBottomSheet(signature) //show bottom sheet with delete and download button
            }
        })
        //recycler view stuff
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        //once data is shown in recycler view, disable prgress bar
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("ResourceAsColor")
    private fun showBottomSheet(signature: SignatureRequest) {
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