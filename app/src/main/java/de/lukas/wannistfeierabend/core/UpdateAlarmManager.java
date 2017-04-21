package de.lukas.wannistfeierabend.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;
import de.lukas.wannistfeierabend.receiver.UpdateAlarmReceiver;

/**
 * Created by Lukas on 16.04.2017.
 */

public class UpdateAlarmManager extends MyAlarmManger {

    Intent updateAlarmIntent;
    PendingIntent updateIntent;

    public UpdateAlarmManager(Context context) {
        super(context);
        updateAlarmIntent = new Intent(context, UpdateAlarmReceiver.class);
    }

    public void setUpdateAlarm(){
        String cycle = sharedPreferences.getString("key_update_interval", "0");
        int interval = 1;
        if (cycle.equals("0")){
            //Täglich
            interval = 1;
        }else{
            // Wöchentlich
            interval = 7;
        }
        updateIntent = PendingIntent.getBroadcast(context, 101, updateAlarmIntent, 0);

        // set update alarm manager to check if a new version is available
        alarmManager.set(AlarmManager.RTC,setTomorrow(17,0,interval),updateIntent);
    }

    public void cancelUpdateAlarm(){
        PendingIntent.getBroadcast(context, 101, updateAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        Log.d("UpdateAlarmManager", "cancelled update alarm.");
    }

    private long setTomorrow(int hour, int minute,int interval){
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_WEEK,interval);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        Log.d("UpdateAlarmManager","set Time for next update check: " + c.getTime());
        return c.getTimeInMillis();
    }

}
