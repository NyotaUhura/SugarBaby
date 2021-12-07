package com.example.sbaby.calendar

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.fragmentViewModel
import com.applandeo.materialcalendarview.EventDay
import com.example.sbaby.MvRxBaseFragment
import com.example.sbaby.R
import com.example.sbaby.databinding.FragmentCalendarBinding
import com.example.sbaby.epoxy.simpleController
import java.util.*


class CalendarFragment : MvRxBaseFragment(R.layout.fragment_calendar) {

    private val binding: FragmentCalendarBinding by viewBinding()
    private val viewModel: CalendarViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun epoxyController() = simpleController(viewModel) { state ->
        /* val events = state.events.invoke()
         bindEvents(events!!)
         if (events != null) {
             bindEvents(events)
         }*/
    }

    private fun EpoxyController.bindEvents(events: List<EventDay>) {
        val cal = Calendar.getInstance()
        val event = EventDay(cal, R.drawable.ic_back)
        val ev = listOf(event)
        binding.calendarView.setEvents(ev)
    }
}
