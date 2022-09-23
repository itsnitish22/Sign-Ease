package com.teamdefine.signease

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

class DatePicker {

    fun getCalendar(date: Long): MaterialDatePicker<Long> {
//        val today=date.time
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(date)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        return datePicker
    }
}