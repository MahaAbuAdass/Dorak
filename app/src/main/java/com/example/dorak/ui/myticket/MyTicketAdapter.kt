package com.example.dorak.ui.myticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.MyTicketResponse
import com.example.dorak.ui.home.BranchAdapter

class MyTicketAdapter (
    private var myTicketList: List<MyTicketResponse?>,
) : RecyclerView.Adapter<MyTicketAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val service_name : TextView = itemView.findViewById(R.id.tv_service)
        val ticket_number : TextView = itemView.findViewById(R.id.tv_number)
        val branchName: TextView = itemView.findViewById(R.id.tv_location)
        val date : TextView = itemView.findViewById(R.id.tv_date)


        fun bind(ticket: MyTicketResponse) {
            service_name.text = ticket.QNameAr
            branchName.text = ticket.BranchNameAr
            ticket_number.text = ticket.TicketNo
            val formattedDateTime = ticket.RegTime?.substring(0, 10) + " " + ticket.RegTime?.substring(11, 16)
            date.text = formattedDateTime

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_my_ticket, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myTicketList.size
    }

    fun getTopBranch(): MyTicketResponse? {
        return myTicketList.firstOrNull()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = myTicketList[position]
        holder.branchName.text = item?.BranchNameAr
        holder.ticket_number.text = item?.TicketNo
        holder.service_name.text = item?.QNameAr
        val formattedDateTime = item?.RegTime?.substring(0, 10) + " " + item?.RegTime?.substring(11, 16)
        holder.date.text = formattedDateTime




    }
}