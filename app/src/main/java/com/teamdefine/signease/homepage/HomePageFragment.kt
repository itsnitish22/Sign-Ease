package com.teamdefine.signease.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        //on swipe refresh, call the api can observer changes
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSignatureRequests()
        }

        return binding.root
    }

    private fun sendSignRequestsToRecycler(signatureRequests: ArrayList<SignatureRequest>) {
        adapter = HomePageAdapter(signatureRequests, object : HomePageAdapter.ItemClickListener {
            override fun onItemClick(signature: SignatureRequest, position: Int) {
                performOptionsMenuClick(signature, position)
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.progressBar.visibility = View.GONE
    }

    private fun performOptionsMenuClick(signature: SignatureRequest, position: Int) {
        val popupMenu =
            PopupMenu(activity, binding.recyclerView[position].findViewById(R.id.optionsMenu))
        popupMenu.inflate(R.menu.main_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.downloadOption -> {
                        Log.i("HomePageFrag Menu", "Download ${signature.subject}")
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    //checking and returning if the user is logged in or not
    private fun checkUser(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null)
            return true
        return false
    }
}