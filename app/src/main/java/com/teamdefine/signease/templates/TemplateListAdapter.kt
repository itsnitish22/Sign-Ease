package com.teamdefine.signease.templates

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.R

class TemplateListAdapter(
    private val templateList: ArrayList<Pair<String, String>>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TemplateListAdapter.ViewHolder>() {

    //interface for handling clicks
    interface ItemClickListener {
        fun onItemClick(template: Pair<String, String>)
    }

    //view holder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView2)
    }

    //to create the views of Recycler View items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemtemplate, parent, false)
        return ViewHolder(v)
    }

    //to bind the fields with the data
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = templateList[position].second
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(templateList[position])
        }
    }

    //returning size of array list
    override fun getItemCount(): Int {
        return templateList.size
    }
}