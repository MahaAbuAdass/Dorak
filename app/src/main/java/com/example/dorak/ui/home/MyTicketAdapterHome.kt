package com.example.dorak.ui.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.dataclass.BranchResponse
import com.example.dorak.dataclass.MyTicketResponse
import com.example.dorak.ui.home.BranchAdapter

class MyTicketAdapterHome
(
    private var myTicketList: List<MyTicketResponse?>,
    private val onItemClick: (MyTicketResponse) -> Unit
) : RecyclerView.Adapter<MyTicketAdapterHome.ItemViewHolder>() {

    private val maxItems = 2
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val service_name : TextView = itemView.findViewById(R.id.tv_service)
//        val ticket_number : TextView = itemView.findViewById(R.id.tv_number)
//        val branchName: TextView = itemView.findViewById(R.id.tv_location)
        val date : TextView = itemView.findViewById(R.id.tv_date)
        val viewButton : Button = itemView.findViewById(R.id.cell_view)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_my_ticket_home, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return minOf(myTicketList.size, maxItems) // Prevents accessing an index out of bounds
    }

    fun getTopBranch(): MyTicketResponse? {
        return myTicketList.firstOrNull()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position >= myTicketList.size) return // Prevents accessing an invalid index

        val item = myTicketList[position] ?: return

        val formattedDateTime = item.RegTime?.substring(0, 10) + "  " + item.RegTime?.substring(11, 16)
        holder.date.text = formattedDateTime

        holder.viewButton.setOnClickListener {
            onItemClick(item)

        }
    }

}