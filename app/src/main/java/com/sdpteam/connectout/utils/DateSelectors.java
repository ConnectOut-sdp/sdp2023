package com.sdpteam.connectout.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.widget.Button;
import android.widget.EditText;

public class DateSelectors {

    public static void setDatePickerDialog(Context context, Button btnDatePicker, EditText txtDate) {
        btnDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, monthOfYear, dayOfMonth) -> txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1), year, month, day);

            // Adding a restriction: user cannot select a date in the past
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            datePickerDialog.show();
        });
    }

    public static void setTimePickerDialog(Context context, Button btnTimePicker, EditText txtTime) {
        btnTimePicker.setOnClickListener(v -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                    (view, hourOfDay, minute1) -> txtTime.setText(hourOfDay + ":" + minute1), hour, minute, false);
            timePickerDialog.show();
        });
    }

    public static long parseEditTextTimeAndDate(EditText txtDate, EditText txtTime) {
        final String[] yearMonthDay = txtDate.getText().toString().split("-"); //[day, month, year]
        final String[] hourMin = txtTime.getText().toString().split(":"); //[hour, min]
        //final Date date;
        final long date;
        if (yearMonthDay.length == 3 && hourMin.length == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
            calendar.set(Calendar.YEAR, Integer.valueOf(yearMonthDay[2]));
            calendar.set(Calendar.MONTH, Integer.valueOf(yearMonthDay[1]) - 1); // Calendar.MONTH starts from 0
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(yearMonthDay[0]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hourMin[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(hourMin[1]));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            date = calendar.getTimeInMillis();
        } else {
            //TODO should fail if the time is not appropriate (No event shouldn't have a time)
            //we said that we would do this in a future sprint task
            date = -666;
        }
        return date;
    }
}
