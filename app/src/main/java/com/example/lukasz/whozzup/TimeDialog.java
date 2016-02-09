package com.example.lukasz.whozzup;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Vikay on 06/02/2016.
 */
public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    EditText editText7;
    public TimeDialog(View view){
        editText7=(EditText)view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c= Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        String time= hourOfDay+":"+minute;
        editText7.setText(time);
    }
}
