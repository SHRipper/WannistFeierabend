package de.lukas.wannistfeierabend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TimePicker;

/**
 * Created by Lukas on 08.04.2017.
 *
 * Creates two TimePickerDialogs. One for the Start of the Interval and one for the end.
 * It automatically set the default SharedPreference of the given Preference object to the Interval String.
 */

public class TimeIntervalPreference{

    String summary = "";

    Context context;

    Preference preference;

    TimePickerDialog startIntervalDialog;
    TimePickerDialog endIntervalDialog;
    TimePickerDialog.OnTimeSetListener startIntervallSetListener;
    TimePickerDialog.OnTimeSetListener endIntervallSetListener;

    /**
     * Initialize the class with needed information.
     *
     * @param preference : The preference object that should be modified.

     */
    public TimeIntervalPreference(final Preference preference, Context context){
        this.preference = preference;
        this.context = context;

        endIntervalDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("TimeIntervalPreference","end time was set to: " + hourOfDay + ":" + minute);
                summary += String.format("%d:%02d", hourOfDay,minute);
                preference.setSummary(summary);
                setPreference();
            }
        }, 13, 0, true);

        startIntervalDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("TimeIntervalPreference","start time was set to: " + hourOfDay + ":" + minute);
                summary += String.format("%d:%02d - ",hourOfDay,minute);
                endIntervalDialog.show();
            }
        }, 8, 0, true);
        startIntervalDialog.show();

    }


    private void setPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preference.getKey(), summary).apply();
        Log.d("TimeIntervallPreference","preference content: " + sharedPreferences.getString(preference.getKey(),"failed"));
    }

}
