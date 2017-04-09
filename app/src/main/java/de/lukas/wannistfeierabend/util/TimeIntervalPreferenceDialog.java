package de.lukas.wannistfeierabend.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import de.lukas.wannistfeierabend.R;

/**
 * Created by Lukas on 08.04.2017.
 *
 * Creates two TimePickerDialogs. One for the Start of the Interval and one for the end.
 * It automatically set the default SharedPreference of the given Preference object to the Interval String.
 */

public class TimeIntervalPreferenceDialog {

    int startInMinutes;
    int endInMinutes;

    String summary = "";

    Context context;

    Preference preference;
    SharedPreferences sharedPreferences;

    TimePickerDialog startIntervalDialog;
    TimePickerDialog endIntervalDialog;
    TimePickerDialog.OnTimeSetListener startIntervallSetListener;
    TimePickerDialog.OnTimeSetListener endIntervallSetListener;

    /**
     * Initialize the class with needed information.
     *
     * @param preference : The preference object that should be modified.
     * @param context : context from the calling fragment or activity
     */
    public TimeIntervalPreferenceDialog(final Preference preference, final Context context){
        this.preference = preference;
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void showDialog(){
        int initTimes[] = getInitTimes();

        endIntervalDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("IntervalPreference","end time was set to: " + hourOfDay + ":" + minute);
                endInMinutes = hourOfDay *60 + minute;
                summary += String.format("%d:%02d", hourOfDay,minute);

                if (endInMinutes < startInMinutes){
                    // start can not be later that the end
                    Toast.makeText(context, context.getString(R.string.error_de_end_before_start),
                            Toast.LENGTH_LONG).show();
                }else{
                    // set summary and save
                    preference.setSummary(summary);
                    setPreference();
                }
            }
        }, initTimes[2], initTimes[3], true);

        startIntervalDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d("IntervalPreference","start time was set to: " + hourOfDay + ":" + minute);
                startInMinutes = hourOfDay*60 + minute;
                summary += String.format("%d:%02d - ",hourOfDay,minute);
                endIntervalDialog.show();
            }
        }, initTimes[0], initTimes[1], true);
        startIntervalDialog.show();
    }

    /**
     * Returns an array of the current time values saved for the given preference key
     * @return 0: start hour <br>
     *         1: start minute <br>
     *         2: end hour <br>
     *         3: end minute
     */
    private int[] getInitTimes(){
        int times[] = new int[4];
        String time[] = sharedPreferences.getString(preference.getKey(),"8:00 - 13:00").split(" - ");
        Log.d("",""+ time[0]);
        String startTime = time[0];
        String endTime = time[1];
        times[0] = Integer.parseInt(startTime.split(":")[0]);
        times[1] = Integer.parseInt(startTime.split(":")[1]);

        times[2] = Integer.parseInt(endTime.split(":")[0]);
        times[3] = Integer.parseInt(endTime.split(":")[1]);
        return times;
    }

    private void setPreference(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preference.getKey(), summary).apply();
        Log.d("TimeIntervallPreference","preference content: " + sharedPreferences.getString(preference.getKey(),"failed"));
    }

}
