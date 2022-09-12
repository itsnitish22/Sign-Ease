package com.teamdefine.signease.templates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.R

class TemplateListAdapter:RecyclerView.Adapter<TemplateListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var textView:TextView=itemView.findViewById(R.id.textView2)
    }

//    To create the views of Recycler View items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.itemtemplate,parent,false)
        return ViewHolder(v)
    }

//    To bind the fields with the data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text="Hello"
    }

//    To return the number of items
    override fun getItemCount(): Int {
        return 5
    }
}