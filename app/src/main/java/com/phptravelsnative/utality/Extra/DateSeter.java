package com.phptravelsnative.utality.Extra;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.phptravelsnative.R;

import java.util.Calendar;


public class DateSeter {


    Context context;
    Calendar c;
    Calendar calendar = Calendar.getInstance();
    private TextView titlebar;

    int year,month,day;

    public DateSeter(Context c) {
        context = c;
    }

    public String getCurrentDay()
    {
        int a=calendar.get(Calendar.DAY_OF_MONTH);
        if(a<10)
           return "0"+a;
        else
        return a+"";
    }
    public  String theMonth(int month){
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return monthNames[month];
    }
    public String getCurrentMonth()
    {

        return theMonth(calendar.get(Calendar.MONTH));
    }
    public String getCurrentTime()
    {

        return calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentMonthN()
    {

        return calendar.get(Calendar.MONTH)+1;
    }
    public int getCurrentYear()
    {
        return calendar.get(Calendar.YEAR);
    }
    public void incrementBy()
    {
        calendar.add(Calendar.DATE,1);
    }
    public void CallenderReset()
    {
        calendar = Calendar.getInstance();
    }
    public  DatePickerDialog ShowDailog(DatePickerDialog.OnDateSetListener listener_data)
    {
        DatePickerDialog date=new DatePickerDialog(context, R.style.DialogTheme,listener_data,year,month,day);
        titlebar=new TextView(context);
        titlebar.setText(R.string.check_in);
        titlebar.setTextSize(18);
        titlebar.setGravity(Gravity.CENTER);
        titlebar.setPadding(20,20,20,20);
        date.setCustomTitle(titlebar);
        date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        CallenderReset();
        date.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        return date;
    }

}
