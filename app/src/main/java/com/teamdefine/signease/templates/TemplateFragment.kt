package com.teamdefine.signease.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.databinding.FragmentTemplateBinding

class TemplateFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null //layout of recycler
    private var adapter: RecyclerView.Adapter<TemplateListAdapter.ViewHolder>? =
        null // recycler adapter
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var viewModel: TemplateListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(requireActivity())[TemplateListViewModel::class.java]

        viewModel.getTemplates()

        return binding.root
    }
}