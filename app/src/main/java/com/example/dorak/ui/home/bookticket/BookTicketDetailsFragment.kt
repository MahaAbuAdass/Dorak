package com.example.dorak.ui.home.bookticket

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dorak.R
import com.example.dorak.databinding.BookTicketDetailsFragmentBinding
import com.example.dorak.dataclass.TimeSlotsResponse
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.viewmodels.GetAvailableTimeViewModel
import com.example.dorak.viewmodels.GetTimeSlotViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class BookTicketDetailsFragment : Fragment() {

    private lateinit var binding: BookTicketDetailsFragmentBinding
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var getAvailableTimeViewModel: GetAvailableTimeViewModel
    private lateinit var getTimeSlotsViewModel: GetTimeSlotViewModel

    private val args: BookTicketDetailsFragmentArgs by navArgs()
    private val enabledDates: HashSet<CalendarDay> = hashSetOf()

    // List of enabled dates
//    private val enabledDates = hashSetOf(
//        CalendarDay.from(2025, 2, 10),
//        CalendarDay.from(2025, 2, 15),
//        CalendarDay.from(2025, 2, 20)
//    )

    // Track the currently selected date
    private var selectedDate: CalendarDay? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BookTicketDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getTimeFactory = GenericViewModelFactory(GetAvailableTimeViewModel::class) {
            GetAvailableTimeViewModel(requireContext())
        }

        getAvailableTimeViewModel =
            ViewModelProvider(this, getTimeFactory).get(GetAvailableTimeViewModel::class.java)


        val getTimeSlotsFactory = GenericViewModelFactory(GetTimeSlotViewModel::class) {
            GetTimeSlotViewModel(requireContext())
        }

        getTimeSlotsViewModel =
            ViewModelProvider(this, getTimeSlotsFactory).get(GetTimeSlotViewModel::class.java)



        binding.title.text = args.location
        calendarView = binding.calendarView

        // Disable the default selection color
        calendarView.selectionColor = Color.TRANSPARENT

        // Add decorators
//        calendarView.addDecorators(
//            DisableAllExceptEnabledDays(enabledDates),
//            EnableHighlightedDays(requireContext(), enabledDates) // Make enabled dates green
//        )

//        // Set a click listener for enabled dates
//        calendarView.setOnDateChangedListener { _, date, selected ->
//            if (enabledDates.contains(date)) {
//                if (selected) {
//                    // Remove the previous selected date decorator
//                    selectedDate?.let {
//                        calendarView.removeDecorators() // Remove all decorators
//                        calendarView.addDecorators(
//                            DisableAllExceptEnabledDays(enabledDates),
//                            EnableHighlightedDays(requireContext(), enabledDates)
//                        )
//                    }
//
//                    // Add the green circle decorator for the newly selected date
//                    calendarView.addDecorator(SelectedDayDecorator(requireContext(), date))
//                    selectedDate = date
//                }
//                showPopup(date)
//            }
//        }

        callGetAvailableTimeApi()
        observerGetAvailableTimeViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observerGetAvailableTimeViewModel() {
        // Creating a map to hold CalendarDay as the key and Pair(SID, DateEffectiveFrom) as the value
        val dateMap = mutableMapOf<CalendarDay, Pair<Int?, String?>>()

        getAvailableTimeViewModel.availableTimeResponse.observe(viewLifecycleOwner) { availableDateList ->
            val newEnabledDates = availableDateList.mapNotNull { response ->
                response.DateEffectiveFrom?.let { dateString ->
                    try {
                        val localDate =
                            LocalDate.parse(dateString.substring(0, 10)) // Extract YYYY-MM-DD
                        Log.e("Date Parsing", localDate.toString())

                        // Create CalendarDay from the date
                        val calendarDay = CalendarDay.from(
                            localDate.year,
                            localDate.monthValue - 1,
                            localDate.dayOfMonth
                        )

                        // Add entry to dateMap
                        dateMap[calendarDay] = Pair(response.SID, dateString)

                        // Return the calendar day for enabled dates
                        calendarDay
                    } catch (e: Exception) {
                        Log.e("Date Parsing", "Error parsing date: $dateString", e)
                        null
                    }
                }
            }.toHashSet()

            enabledDates.clear()
            enabledDates.addAll(newEnabledDates)

            // Update calendar decorators
            calendarView.removeDecorators()
            calendarView.addDecorators(
                DisableAllExceptEnabledDays(enabledDates),
                EnableHighlightedDays(requireContext(), enabledDates)
            )

            // Set a click listener for enabled dates
            calendarView.setOnDateChangedListener { _, date, selected ->
                if (enabledDates.contains(date)) {
                    if (selected) {
                        selectedDate?.let {
                            calendarView.removeDecorators() // Remove all decorators
                            calendarView.addDecorators(
                                DisableAllExceptEnabledDays(enabledDates),
                                EnableHighlightedDays(requireContext(), enabledDates)
                            )
                        }

                        // Highlight the selected date with a green circle
                        calendarView.addDecorator(SelectedDayDecorator(requireContext(), date))
                        selectedDate = date

                        // Fetch SID and DateEffectiveFrom from the dateMap
                        val pair = dateMap[date] ?: return@setOnDateChangedListener
                        val (sid, fullDate) = pair // Destructure the pair to get SID and DateEffectiveFrom

                        // Call your timeSlots function with SID and DateEffectiveFrom
                        callTimeSlotsApi(fullDate ?: "", sid.toString())
                        observerTimeSlotsAPi()
                    }
                }
            }
        }


        getAvailableTimeViewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.v("error response", "Error fetching available dates")

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun callTimeSlotsApi(selectedDate: String, sid: String) {
        Log.v("selected date 34", selectedDate)
        Log.v("selected date 34", sid)
        val branchCode = args.branchCode

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            getTimeSlotsViewModel.getTimeSlots(selectedDate, branchCode, sid)
        }
//        val formattedDate = "${selectedDate.year}-${String.format("%02d", selectedDate.month + 1)}-${String.format("%02d", selectedDate.day)}"
//        Log.v("selected date",formattedDate.toString())

//        observerTimeSlotsAPi()

    }

    private fun observerTimeSlotsAPi() {
        getTimeSlotsViewModel.timeSlotsResponse.observe(viewLifecycleOwner) {timeSlotList->
            timeSlotAdapter(timeSlotList)
        }

        getTimeSlotsViewModel.errorResponse.observe(viewLifecycleOwner) {
            Log.v("error retrieve time slots", "")
        }
    }

    private fun timeSlotAdapter(timeSlotList: List<TimeSlotsResponse>?) {
        val recyclerView = binding.timeSlotsRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3 items per row
        recyclerView.adapter = timeSlotList?.let {
            TimeSlotAdapter(it) { isSelected ->
                binding.btnConfirm.isEnabled = isSelected

            }
        }}


    private fun handleTimeSlotClick(selectedTimeSlot: TimeSlotsResponse) {

        // Example: Pass data to another fragment using Navigation Component


        // OR show a dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Time Slot Selected")
            .setMessage("You selected: ${selectedTimeSlot.Slot}")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }



    private fun callGetAvailableTimeApi() {
        val branchCode = args.branchCode
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            getAvailableTimeViewModel.getAvailableTime(branchCode ?: "")
        }
    }

    private fun showPopup(date: CalendarDay) {
        AlertDialog.Builder(requireContext())
            .setTitle("Date Selected")
            .setMessage("You selected: ${date.year}-${date.month}-${date.day}")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // Decorator to disable all days except enabled ones
    class DisableAllExceptEnabledDays(private val enabledDays: Set<CalendarDay>) :
        DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return !enabledDays.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true) // Make them unclickable
            view.addSpan(ForegroundColorSpan(Color.GRAY)) // Make them visually distinct
        }
    }

    // Decorator to enable the allowed days and make their text color green
    class EnableHighlightedDays(
        private val context: Context,
        private val enabledDays: Set<CalendarDay>
    ) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return enabledDays.contains(day)
        }

        @SuppressLint("ResourceAsColor")
        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(false) // Make them clickable
            view.addSpan(ForegroundColorSpan(Color.GREEN)) // Set text color to green
        }
    }

    // Decorator to show a green circle on the selected date
    class SelectedDayDecorator(
        private val context: Context,
        private val selectedDate: CalendarDay
    ) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == selectedDate
        }

        override fun decorate(view: DayViewFacade) {
            // Set a custom green circle drawable for the selected date
            view.setSelectionDrawable(
                ContextCompat.getDrawable(context, R.drawable.custom_selection_circle)!!
            )
        }
    }
}