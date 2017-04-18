package de.lukas.wannistfeierabend.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

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
        String cycle = sharedPreferences.getString("key_update_interval","Wöchentlich");
        long interval = 0;
        if (cycle.equals("Täglich")){
            interval = AlarmManager.INTERVAL_DAY;
        }else{
            // cycle == "Wöchentlich"
            interval = AlarmManager.INTERVAL_DAY * 7;
        }

        updateIntent = PendingIntent.getBroadcast(context, 101, updateAlarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, setTomorrow(18,0),
                interval,updateIntent);

    }

    public void cancelUpdateAlarm(){
    }

    private long setTomorrow(int hour, int minute){
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_WEEK,1);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hour);
        return c.getTimeInMillis();
    }

}
