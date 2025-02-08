package com.example.dorak.ui.home.bookticket

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.databinding.CellTimeSlotBinding
import com.example.dorak.dataclass.TimeSlotsResponse

// TimeSlotsAdapter.kt
class TimeSlotAdapter(
    private val timeSlots: List<TimeSlotsResponse>,
    private val onTimeSlotSelected: (Boolean) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class TimeSlotViewHolder(val binding: CellTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeSlot: TimeSlotsResponse, position: Int) {
            // Keep your existing time extraction logic
            val slot = timeSlot.Slot
            val time = slot?.split("T")?.get(1)?.substring(0, 5) // Get the time after 'T' (HH:mm)
            binding.slotTime.text = time

            // Change background color based on selection
            if (selectedPosition == position) {
                binding.cardView.setCardBackgroundColor(Color.GREEN)
            } else {
                binding.cardView.setCardBackgroundColor(Color.WHITE)
            }

            // Handle click event
            binding.cardView.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onTimeSlotSelected(true) // Enable confirm button
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val binding = CellTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        holder.bind(timeSlots[position], position)
    }

    override fun getItemCount() = timeSlots.size
}

