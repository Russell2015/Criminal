package com.example.android.criminal;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016/3/15.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String TIMER_DATE = "Time date";
    public static final String RETURN_TIMER_DATE = "Return time";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(TIMER_DATE, date);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(args);
        return timePickerFragment;
    }

    private void sendResult(int resultCode, Date date){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(RETURN_TIMER_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker, null);
        mTimePicker = (TimePicker)view.findViewById(R.id.time_picker_time_picker);
        Date mDate = (Date)getArguments().getSerializable(TIMER_DATE);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minutes);

        return new AlertDialog.Builder(getActivity())
                .setTitle("发生的时间")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = mTimePicker.getCurrentHour();
                        int minute = mTimePicker.getCurrentMinute();
                        Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }
}
