package com.example.dorak.ui.home.bookticket

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.dorak.R
import com.example.dorak.databinding.BookTicketDetailsFragmentBinding
import com.example.dorak.network.GenericViewModelFactory
import com.example.dorak.viewmodels.GetAvailableTimeViewModel
import com.example.dorak.viewmodels.GetBranchesViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookTicketDetailsFragment : Fragment() {

    private lateinit var binding: BookTicketDetailsFragmentBinding
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var getAvailableTimeViewModel: GetAvailableTimeViewModel

    private val args: BookTicketDetailsFragmentArgs by navArgs()

    // List of enabled dates
    private val enabledDates = hashSetOf(
        CalendarDay.from(2025, 2, 10),
        CalendarDay.from(2025, 2, 15),
        CalendarDay.from(2025, 2, 20)
    )

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

        getAvailableTimeViewModel = ViewModelProvider(this, getTimeFactory).get(GetAvailableTimeViewModel::class.java)

        binding.title.text = args.location
        calendarView = binding.calendarView

        // Disable the default selection color
        calendarView.selectionColor = Color.TRANSPARENT

        // Add decorators
        calendarView.addDecorators(
            DisableAllExceptEnabledDays(enabledDates),
            EnableHighlightedDays(requireContext(), enabledDates) // Make enabled dates green
        )

        // Set a click listener for enabled dates
        calendarView.setOnDateChangedListener { _, date, selected ->
            if (enabledDates.contains(date)) {
                if (selected) {
                    // Remove the previous selected date decorator
                    selectedDate?.let {
                        calendarView.removeDecorators() // Remove all decorators
                        calendarView.addDecorators(
                            DisableAllExceptEnabledDays(enabledDates),
                            EnableHighlightedDays(requireContext(), enabledDates)
                        )
                    }

                    // Add the green circle decorator for the newly selected date
                    calendarView.addDecorator(SelectedDayDecorator(requireContext(), date))
                    selectedDate = date
                }
                showPopup(date)
            }
        }

        callGetAvailableTimeApi()
        observerGetAvailableTimeViewModel()
    }

    private fun observerGetAvailableTimeViewModel() {
    }

    private fun callGetAvailableTimeApi() {
        val branchCode = args.branchCode
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            getAvailableTimeViewModel.getAvailableTime(branchCode?:"")
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
    class DisableAllExceptEnabledDays(private val enabledDays: Set<CalendarDay>) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return !enabledDays.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true) // Make them unclickable
            view.addSpan(ForegroundColorSpan(Color.GRAY)) // Make them visually distinct
        }
    }

    // Decorator to enable the allowed days and make their text color green
    class EnableHighlightedDays(private val context: Context, private val enabledDays: Set<CalendarDay>) : DayViewDecorator {
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