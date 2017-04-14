package de.lukas.wannistfeierabend.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import de.lukas.wannistfeierabend.receiver.AlarmReceiver;
import de.lukas.wannistfeierabend.util.TimeManager;

/**
 * Alarm needs to be initialized, when app is launched and when device has finished booting
 * see https://developer.android.com/training/scheduling/alarms.html#boot
 */
public class MyAlarmManger {

    AlarmManager alarmManager;
    PendingIntent pendingIntent25, pendingIntent50, pendingIntent75;
    Intent alarmIntent25, alarmIntent50, alarmIntent75;
    SharedPreferences sharedPreferences;
    Context context;


    public MyAlarmManger(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("InitAlarmManager", "initializing alarm manager");
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void setAllAlarms() {
        setAlarm25();
        setAlarm50();
        setAlarm75();
    }

    public void setNextAlarm(int currentNotificationID){
        switch (currentNotificationID){
            case 25:
                setAlarm50();
                break;
            case 50:
                setAlarm75();
                break;
            case 75:
                setAlarm25();
                break;
            case 0:
                Log.d("MyAlarmManager", "find next alarm to set");
                setNextAlarm(findNextAlarmID());
        }
    }

    private int findNextAlarmID(){
        TimeManager tm = new TimeManager(context);
        int done = tm.getTimePeriodDone();
        int total = tm.getTotalPeriodForToday();
        int percent = (int)((done / (double)total) *100);
        Log.d("MyAlarmManger", "percent done: " + percent);
        if (percent >= 25 && percent < 50){
            return 50;
        }
        if (percent >= 50 && percent < 75){
            return 75;
        }
        if (percent >= 75 || percent < 25){
            return 25;
        }
        return 1;
    }

    public void setAlarm25() {
        alarmIntent25 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent25.putExtra("id", 25);
        boolean notif25 = sharedPreferences.getBoolean("key_notifications_25", false);
        boolean alarm25AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm25AlreadySet && notif25) {
            Log.d("InitAlarmManager", "alarm for 25% was not set before. Setting 25% alarm");
            pendingIntent25 = PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context, 25), pendingIntent25);
        } else {
            Log.d("InitAlarmManager", "alarm for 25% was set before. do nothing.");
        }
    }

    public void setAlarm50() {
        alarmIntent50 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent50.putExtra("id", 50);
        boolean notif50 = sharedPreferences.getBoolean("key_notifications_50", false);
        boolean alarm50AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm50AlreadySet && notif50) {
            Log.d("InitAlarmManager", "setting 50% alarm");
            pendingIntent50 = PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context, 50), pendingIntent50);
        } else {
            Log.d("InitAlarmManager", "alarm for 50% not set. setBefore? " + alarm50AlreadySet + " ; 50 enabled? " + notif50);
        }
    }

    private void setAlarm75() {
        alarmIntent75 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent75.putExtra("id", 75);
        boolean notif75 = sharedPreferences.getBoolean("key_notifications_75", false);
        boolean alarm75AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm75AlreadySet && notif75) {
            Log.d("InitAlarmManager", "alarm for 75% was not set before. Setting 75% alarm");
            pendingIntent75 = PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context, 75), pendingIntent75);
        } else {
            Log.d("InitAlarmManager", "alarm for 75% was set before. do nothing.");
        }
    }

    public void cancelAllAlarms() {
        boolean notif75 = sharedPreferences.getBoolean("key_notifications_75", false);
        boolean notif50 = sharedPreferences.getBoolean("key_notifications_50", false);
        boolean notif25 = sharedPreferences.getBoolean("key_notifications_25", false);

        if (notif25) {
            PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 25%.");

        }
        if (notif50) {
            PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 50%.");

        }
        if (notif75) {
            PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 75%.");
        }

    }

    private long getNextAlarmMillis(Context context, int percent) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + (61 * 1000));
        Log.d("InitAlarmManager", "triggerTime: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        Log.d("InitAlarmManager", "triggerDate: " + cal.get(Calendar.DAY_OF_MONTH) + ":" + cal.get(Calendar.MONTH));

        //String[] parts = triggerTime.split(" ");
        Log.d("Calendar", "" + cal.get(Calendar.HOUR_OF_DAY) + " . " + cal.get(Calendar.MINUTE));

        return cal.getTimeInMillis();

    }
}
