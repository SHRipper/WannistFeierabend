package de.lukas.wannistfeierabend.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.TimePicker;

/**
 * Created by Lukas on 09.04.2017.
 */

public class TimePreferenceDialog implements TimePickerDialog.OnTimeSetListener{

    Context context;
    Preference preference;
    int hour, minutes;

    SharedPreferences sharedPreferences;

    public TimePreferenceDialog(Preference preference, Context context){
        this.context = context;
        this.preference = preference;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int times[]  = getInitTime();
        this.hour = times[0];
        this.minutes = times[1];
    }

    public void showDialog(){
        new TimePickerDialog(context,this,hour,minutes,true).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        sharedPreferences.edit().putString(preference.getKey(),String.format("%d:%02d",i,i1 )).apply();
        updateSummary();
    }

    /**
     * Returns an array of the current time values saved for the given preference key
     * @return 0: initial hour <br>
     *         1: initial minute <br>
     */
    private int[] getInitTime(){
        int times[] = new int[2];
        String time = sharedPreferences.getString(preference.getKey(),"8:00");

        times[0] = Integer.parseInt(time.split(":")[0]);
        times[1] = Integer.parseInt(time.split(":")[1]);

        return times;
    }

    private void updateSummary(){
        preference.setSummary(sharedPreferences.getString(preference.getKey(), "8:01"));
    }
}
