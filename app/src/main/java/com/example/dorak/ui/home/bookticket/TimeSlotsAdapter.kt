package com.example.dorak.ui.home.bookticket

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dorak.R
import com.example.dorak.databinding.CellTimeSlotBinding
import com.example.dorak.dataclass.TimeSlotsResponse


// TimeSlotsAdapter.kt
class TimeSlotAdapter(
    private val timeSlots: List<TimeSlotsResponse>,
    private val context: Context,
    private val onTimeSlotSelected: (String) -> Unit // Pass selected time
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private var selectedTime: String? = null

    inner class TimeSlotViewHolder(val binding: CellTimeSlotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(timeSlot: TimeSlotsResponse, position: Int) {
            val slot = timeSlot.Slot
            val time = slot?.split("T")?.get(1)?.substring(0, 5) // Get the time in HH:mm format
            binding.slotTime.text = time

            // Change background color based on selection
            if (selectedPosition == position) {
                val greenColor = ContextCompat.getColor(context, R.color.green)
                binding.cardView.setCardBackgroundColor(greenColor)
            } else {
                val whiteColor = ContextCompat.getColor(context, R.color.white)
                binding.cardView.setCardBackgroundColor(whiteColor)
            }

            // Handle click event
            binding.cardView.setOnClickListener {
                selectedPosition = position
                selectedTime = time // Store selected time
                notifyDataSetChanged()
                onTimeSlotSelected(time ?: "") // Send selected time to fragment
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
