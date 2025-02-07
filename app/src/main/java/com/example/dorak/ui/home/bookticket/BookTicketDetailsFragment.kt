package com.example.dorak.ui.home.bookticket

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.dorak.R
import com.example.dorak.databinding.BookTicketDetailsFragmentBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.time.LocalDate


class BookTicketDetailsFragment : Fragment() {

    private lateinit var binding : BookTicketDetailsFragmentBinding

//    private val args: BookTicketDetailsFragmentArgs by navArgs()

    private lateinit var calendarView: MaterialCalendarView
    private val enabledDates = listOf(5, 10, 15, 20) // Days to enable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BookTicketDetailsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendarView


        // List of enabled dates
         val enabledDates = hashSetOf(
            CalendarDay.from(2025, 2, 10),
            CalendarDay.from(2025, 2, 15),
            CalendarDay.from(2025, 2, 20)
        )

        calendarView.addDecorator(DisableAllExceptEnabledDays(enabledDates))

        // Click listener for enabled dates
        calendarView.setOnDateChangedListener { _, date, _ ->
            if (enabledDates.contains(date)) {
                showPopup(date)
            }
        }

    }
    private fun showPopup(date: CalendarDay) {
        AlertDialog.Builder(requireContext())
            .setTitle("Date Selected")
            .setMessage("You selected: ${date.year}-${date.month + 1}-${date.day}")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    class DisableAllExceptEnabledDays(private val enabledDays: Set<CalendarDay>) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return !enabledDays.contains(day) // Disable non-enabled dates
        }

        override fun decorate(view: DayViewFacade) {
            view.setDaysDisabled(true) // Makes them unclickable
        }
    }

        // Set current month & year in the title
//        val currentDate = CalendarDay.today()
//        calendarView.setTitleFormatter { "${currentDate.month} ${currentDate.year}" }
//
//        // Mark enabled/disabled days
//        calendarView.addDecorators(DisabledDaysDecorator(), EnabledDaysDecorator(enabledDates))


   //     binding.title.text = args.location
   // }

//
//    inner class DisabledDaysDecorator : DayViewDecorator {
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            return day != null && day.day !in enabledDates
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.setDaysDisabled(true)
//        }
//    }
//
//    // Decorator to enable specific days
//    inner class EnabledDaysDecorator(private val enabledDays: List<Int>) : DayViewDecorator {
//        override fun shouldDecorate(day: CalendarDay?): Boolean {
//            return day != null && day.day in enabledDays
//        }
//
//        override fun decorate(view: DayViewFacade?) {
//            view?.setDaysDisabled(false)
//            view?.addSpan(ForegroundColorSpan(Color.GREEN)) // Highlight enabled days
//        }
//    }
}