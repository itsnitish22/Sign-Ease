package com.teamdefine.signease.templates

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.R
import com.teamdefine.signease.api.modelsgetrequest.Templates
import com.teamdefine.signease.confirmation.ConfirmationFragment

class TemplateListAdapter(private val templateList:ArrayList<String>) : RecyclerView.Adapter<TemplateListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView2)
    }

    //    To create the views of Recycler View items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemtemplate, parent, false)
        return ViewHolder(v)
    }

    //    To bind the fields with the data
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = templateList[position]
        holder.itemView.setOnClickListener {
//            val date=ConfirmationFragment().getCalendar()
//            Log.i("helloabc3",date)
            Log.i("helloabc3",templateList[position])
        }
    }

    //    To return the number of items
    override fun getItemCount(): Int {
        return templateList.size
    }
}