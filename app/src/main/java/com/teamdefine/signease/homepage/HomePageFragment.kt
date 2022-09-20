package com.teamdefine.signease.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: HomeFragmentViewModel
    var firebaseData = mutableMapOf<String, Any>() //global variable which will store user data
    private var adapter: RecyclerView.Adapter<HomePageAdapter.ViewHolder>? = null

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        firebaseAuth = FirebaseAuth.getInstance()

        //checking if user is logged in or not
        val loggedIn = checkUser()
        if (loggedIn) { //if logged in
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getSignatureRequests() // getting all signature requests made till date
            viewModel.getDataFromFirestore()
        } else
            Toast.makeText(activity, "Log in first", Toast.LENGTH_SHORT).show()

        //observing changes in received signature requests
        viewModel.requests.observe(requireActivity()) { requests ->
            if (binding.swipeRefresh.isRefreshing)
                binding.swipeRefresh.isRefreshing = false

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            binding.totalRequests.text = requests.list_info.num_results.toString()
            binding.lastUpdated.text = "Last Updated: $currentDate"

            sendSignRequestsToRecycler(requests.signature_requests)
        }

        viewModel.data.observe(requireActivity()) { data ->
            val firstName = data["fullName"].toString().substringBefore(" ", "Not Found")
            binding.welcomeText.text = "Welcome $firstName"
        }

        viewModel.url.observe(requireActivity()) { url ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSignatureRequests()
        }

        return binding.root
    }

    private fun sendSignRequestsToRecycler(signatureRequests: ArrayList<SignatureRequest>) {
        adapter = HomePageAdapter(signatureRequests, object : HomePageAdapter.ItemClickListener {
            override fun onItemClick(signature: SignatureRequest, position: Int) {
                showBottomSheet(signature)
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.progressBar.visibility = View.GONE
    }

    private fun showBottomSheet(signature: SignatureRequest) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val downloadButton = view.findViewById<Button>(R.id.downloadButton)
        val showNameText = view.findViewById<TextView>(R.id.subjectRequest)
        showNameText.text = signature.subject
        downloadButton.setOnClickListener {
            viewModel.getFileUrl(signature.signature_request_id)
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun downloadFile(signatureRequestId: String) {
        viewModel.getFileUrl(signatureRequestId)
    }

    //checking and returning if the user is logged in or not
    private fun checkUser(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}