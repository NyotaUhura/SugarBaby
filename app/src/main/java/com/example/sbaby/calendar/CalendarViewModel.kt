package com.example.sbaby.calendar

import com.airbnb.mvrx.*
import com.applandeo.materialcalendarview.EventDay

data class CalendarState(
    val events: Async<List<EventDay>> = Uninitialized
): MavericksState

class CalendarViewModel(
    initialState: CalendarState
): MavericksViewModel<CalendarState>(initialState) {
    init {

    }

    companion object: MavericksViewModelFactory<CalendarViewModel, CalendarState> {

        override fun create(viewModelContext: ViewModelContext, state: CalendarState): CalendarViewModel? {
            return CalendarViewModel(state)
        }
    }
}