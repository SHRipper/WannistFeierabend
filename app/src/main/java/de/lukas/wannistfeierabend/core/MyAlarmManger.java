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
    TimeManager tm;
    Context context;


    public MyAlarmManger(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        tm = new TimeManager(context);

        Log.d("InitAlarmManager", "initializing alarm manager");

        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmIntent25 = new Intent(context, AlarmReceiver.class);
        alarmIntent25.putExtra("id", 25);
        alarmIntent50 = new Intent(context, AlarmReceiver.class);
        alarmIntent50.putExtra("id", 50);
        alarmIntent75 = new Intent(context, AlarmReceiver.class);
        alarmIntent75.putExtra("id", 75);
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
                setNextAlarm(findNotificationID());
            default:
                Log.wtf("MyAlarmManager","wtf have you done");
        }
    }

    /**
     * Returns the Notification id that should have been
     * fired before from the percentage done.
     * @return
     */
    private int findNotificationID(){
        int percent = tm.getPercentDone();
        Log.d("MyAlarmManger", "percent done: " + percent);
        if (percent >= 25 && percent < 50){
            // percent is betweed 25 and 49 -> the last notification should have been 25.
            return 25;
        }
        else if (percent >= 50 && percent < 75){
            return 50;
        }
        else {
            //percent >= 75 or percent < 25
            return 75;
        }
    }

    public void setAlarm25() {
        boolean notif25 = sharedPreferences.getBoolean("key_notifications_25", false);
        boolean alarm25AlreadySet = (PendingIntent.getBroadcast(context, 25, alarmIntent25, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm25AlreadySet && notif25) {
            Log.d("InitAlarmManager", "alarm for 25% was not set before. Setting 25% alarm");
            pendingIntent25 = PendingIntent.getBroadcast(context, 25, alarmIntent25, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(25), pendingIntent25);
        } else {
            Log.d("InitAlarmManager", "alarm for 25% was set before. do nothing.");
        }
    }

    public void setAlarm50() {

        boolean notif50 = sharedPreferences.getBoolean("key_notifications_50", false);
        boolean alarm50AlreadySet = (PendingIntent.getBroadcast(context, 50, alarmIntent50, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm50AlreadySet && notif50) {
            Log.d("InitAlarmManager", "setting 50% alarm");
            pendingIntent50 = PendingIntent.getBroadcast(context, 50, alarmIntent50, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(50), pendingIntent50);
        } else {
            Log.d("InitAlarmManager", "alarm for 50% not set. setBefore? " + alarm50AlreadySet + " ; 50 enabled? " + notif50);
        }
    }

    private void setAlarm75() {

        boolean notif75 = sharedPreferences.getBoolean("key_notifications_75", false);
        boolean alarm75AlreadySet = (PendingIntent.getBroadcast(context, 75, alarmIntent75, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm75AlreadySet && notif75) {
            Log.d("InitAlarmManager", "alarm for 75% was not set before. Setting 75% alarm");
            pendingIntent75 = PendingIntent.getBroadcast(context, 75, alarmIntent75, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(75), pendingIntent75);
        } else {
            Log.d("InitAlarmManager", "alarm for 75% was set before. do nothing.");
        }
    }

    public void cancelAllAlarms() {
        boolean notif75 = sharedPreferences.getBoolean("key_notifications_75", false);
        boolean notif50 = sharedPreferences.getBoolean("key_notifications_50", false);
        boolean notif25 = sharedPreferences.getBoolean("key_notifications_25", false);

        if (notif25) {
            PendingIntent.getBroadcast(context, 25, alarmIntent25, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 25%.");

        }
        if (notif50) {
            PendingIntent.getBroadcast(context, 50, alarmIntent50, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 50%.");

        }
        if (notif75) {
            PendingIntent.getBroadcast(context, 75, alarmIntent75, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            Log.d("InitAlarmManager", "cancelled alarm 75%.");
        }

    }

    private long getNextAlarmMillisForPercent(int percent) {
        int times[] = tm.getTimeInMinutesForToday();
        int startTime = times[0];
        int period = tm.getTotalPeriodForToday();
        boolean showSaturday = sharedPreferences.getBoolean("key_saturday_show",false);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        long nextAlarmTimeMillis = (startTime + (percent/100) * period)*60*1000;

        Log.d("InitAlarmManager", "triggerTime: " + nextAlarmTimeMillis/(3600*1000) +
                ":" + (nextAlarmTimeMillis/(60*1000))%60);

        if(nextAlarmTimeMillis < System.currentTimeMillis()){
            // The alarm time is scheduled in the past. This is a sign that the
            // next alarm time should be on the next possible day.
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !showSaturday
                    || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                // The alarm is scheduled on monday on following occasions
                // - it is friday and saturday is not enabled
                // - it is saturday
                nextAlarmTimeMillis = setOnMonday(percent);
            }else{
                // The alarm is scheduled on the next day on following occasions
                // - it is Monday - Thursday
                // - it is Friday with Saturday being enabled
                nextAlarmTimeMillis = setTomorrow(percent);
            }
        }

        return nextAlarmTimeMillis;
    }

    /**
     * Set the alarm on monday to the time calculated from the percent value.
     * @param percent
     * @return
     */
    private long setOnMonday(int percent){

    }

    private long setTomorrow(int percent){

    }

}
