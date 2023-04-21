package com.teamdefine.signease.utils

import android.util.Log
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class DatePicker {
    fun getCalendar(date: Long): MaterialDatePicker<Long> {
        Log.i("helloabc", Date(date).toString())
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(date)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    fun getCalendar2(
        date1: Long,
        date2: Long
    ): MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {
        Log.i("helloabc", Date(date1).toString())

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        return MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select date").setSelection(androidx.core.util.Pair(date1, date2))
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }
}