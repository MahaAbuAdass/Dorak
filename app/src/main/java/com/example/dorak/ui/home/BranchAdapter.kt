package com.example.dorak.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.dataclass.BranchResponse

class BranchAdapter  (
    private var branchesList: List<BranchResponse?>,
    private val onItemClick: (BranchResponse) -> Unit
) : RecyclerView.Adapter<BranchAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val branchName: TextView = itemView.findViewById(R.id.branch_name)

        fun bind(branch: BranchResponse) {
            branchName.text = branch.BranchNameEn

            itemView.setOnClickListener {
                onItemClick(branch)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_branch, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return branchesList.size
    }

    fun getTopBranch(): BranchResponse? {
        return branchesList.firstOrNull()
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = branchesList[position]
        holder.branchName.text = item?.BranchNameEn

            item?.let {
                holder.bind(it)  // Calls the bind function, setting text and click listener

        }


    }
}