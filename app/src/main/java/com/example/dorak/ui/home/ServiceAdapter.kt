package com.example.dorak.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.dataclass.ServicesResponse

class ServiceAdapter (
    private var serviceList: List<ServicesResponse?>,
    private val onItemClick: (ServicesResponse) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.service_name)

        fun bind(service: ServicesResponse) {
            serviceName.text = service.QNameEn

            itemView.setOnClickListener {
                onItemClick(service)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_service, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = serviceList[position]
        holder.serviceName.text = item?.QNameEn


        holder.itemView.setOnClickListener {
            item?.let {
                holder.bind(it)  // Calls the bind function, setting text and click listener
            }
        }


    }


}