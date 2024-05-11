package com.example.bytebills.ui.stockvalue;

import android.icu.text.SimpleDateFormat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClaimsXAxisValueFormatter extends ValueFormatter {

    List<String> datesList;

    public ClaimsXAxisValueFormatter(List<String> arrayOfDates) {
        this.datesList = arrayOfDates;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {
/*
Depends on the position number on the X axis, we need to display the label, Here, this is the logic to convert the float value to integer so that I can get the value from array based on that integer and can convert it to the required value here, month and date as value. This is required for my data to show properly, you can customize according to your needs.
*/
        Integer position = Math.round(value);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd hh mm");

        if (position < datesList.size()) {
            return sdf.format(new Date((getDateInMilliSeconds(datesList.get(position), "yyyy-MM-dd-HH-mm"))));
        }
        return "";
    }

    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String day = givenDateString.split("T")[0];
        String time = givenDateString.split("T")[1];
        givenDateString = day + "-" + time;
        givenDateString = givenDateString.split("\\.")[0].replace(":", "-");
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.FRENCH);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
}