package com.teamdefine.signease.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamdefine.signease.R
import com.teamdefine.signease.api.models.get_all_sign_requests.SignatureRequest
import java.text.SimpleDateFormat
import java.util.*

class HomePageAdapter(
    private val signatureList: ArrayList<SignatureRequest>,
    private val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(signature: SignatureRequest, position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfRequest: TextView = itemView.findViewById(R.id.nameOfRequest)
        val timeOfRequest: TextView = itemView.findViewById(R.id.timeOfRequest)
        val statusButton: ImageView = itemView.findViewById(R.id.statusButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_signing_requests, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val signature = signatureList[position]
        holder.nameOfRequest.text = signature.subject
        holder.timeOfRequest.text = convertLongToTime(signature.created_at)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(signatureList[position], position)
        }
//        if (signature.is_complete)
//            holder.statusButton.setColorFilter(Color.GREEN)
//        else
//            holder.statusButton.setColorFilter(Color.YELLOW)
        if (signature.is_complete)
            holder.statusButton.setImageResource(R.drawable.radio_button_2)
        else
            holder.statusButton.setImageResource(R.drawable.radio_button)
    }

    override fun getItemCount(): Int {
        return signatureList.size
    }

    private fun convertLongToTime(createdAt: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val date = simpleDateFormat.format(createdAt * 1000L)
        return date
    }
}