package com.teamdefine.signease.templates

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.domain.models.get_all_templates.Template
import com.teamdefine.signease.R
import java.util.*

class TemplateListAdapter(
    private val templateList: ArrayList<Template>,
    private val itemClickListener: ItemClickListener,
    private val itemEyeClickListener: ItemEyeClickListener
) : RecyclerView.Adapter<TemplateListAdapter.ViewHolder>() {

    //interface for handling clicks
    interface ItemClickListener {
        fun onItemClick(template: Template)
    }

    //interface for eye button
    interface ItemEyeClickListener {
        fun onItemEyeClickListener(template: Template)
    }

    //to create the views of Recycler View items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemtemplate, parent, false)
        return ViewHolder(v)
    }

    //to bind the fields with the data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleOfTemplate.text = templateList[position].title

        val date = templateList[position].updated_at  //extracted unix timestamp from response body
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val dt = simpleDateFormat.format(date * 1000L)  //formatting the timestamp as date only

        holder.date.text = dt
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(templateList[position])
        }
        holder.preview.setOnClickListener {
            itemEyeClickListener.onItemEyeClickListener(templateList[position])
        }
    }

    //returning size of array list
    override fun getItemCount(): Int {
        return templateList.size
    }


    //view holder class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleOfTemplate: TextView = itemView.findViewById(R.id.title)
        var date: TextView = itemView.findViewById(R.id.templateDate)
        var preview: ImageView = itemView.findViewById(R.id.info)
    }
}