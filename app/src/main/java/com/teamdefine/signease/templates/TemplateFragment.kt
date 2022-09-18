package com.teamdefine.signease.templates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.api.modelsgetrequest.Templates
import com.teamdefine.signease.databinding.FragmentTemplateBinding

class TemplateFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null //layout of recycler
    private var adapter: RecyclerView.Adapter<TemplateListAdapter.ViewHolder>? =
        null // recycler adapter
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var viewModel: TemplateListViewModel
    private val templateList:ArrayList<String> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(requireActivity())[TemplateListViewModel::class.java]
        viewModel.getTemplates()
        viewModel.templates.observe(requireActivity(), Observer { template->
            Log.i("helloabc2",template.toString())
            addDataToArrayList(template)
        })

        return binding.root
    }

    private fun addDataToArrayList(template: Templates?) {
        val templates= template?.templates
        if (templates != null) {
            for(i in templates){
                templateList.add(i.title)
            }
            adapter=TemplateListAdapter(templateList)
            binding.recyclerView.adapter = adapter
        }
    }
}