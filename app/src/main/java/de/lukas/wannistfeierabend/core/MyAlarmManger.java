package de.lukas.wannistfeierabend.core;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.AlarmManager;

import java.util.Calendar;

import de.lukas.wannistfeierabend.receiver.AlarmReceiver;

/**
 * Alarm needs to be initialized, when app is launched and when device has finished booting
 * see https://developer.android.com/training/scheduling/alarms.html#boot
 */
public class MyAlarmManger {

    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent25,pendingIntent50,pendingIntent75;
    private static Intent alarmIntent25, alarmIntent50, alarmIntent75;
    private static SharedPreferences sharedPreferences;

    public static void setAlarmManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarmIntent25 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent25.putExtra("id",25);
        alarmIntent50 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent50.putExtra("id",50);
        alarmIntent75 = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        alarmIntent75.putExtra("id",75);
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Log.d("InitAlarmManager", "initializing alarm manager");

        boolean notif25 = sharedPreferences.getBoolean("key_notification_25",false);
        boolean notif50 = sharedPreferences.getBoolean("key_notification_50",false);
        boolean notif75 = sharedPreferences.getBoolean("key_notification_75",false);

        boolean alarm25AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, PendingIntent.FLAG_NO_CREATE) != null);
        boolean alarm50AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, PendingIntent.FLAG_NO_CREATE) != null);
        boolean alarm75AlreadySet = (PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm25AlreadySet && notif25) {
            Log.d("InitAlarmManager", "alarm for 25% was not set before. Setting 25% alarm");
            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context,25), AlarmManager.INTERVAL_DAY, pendingIntent25);
            pendingIntent25 = PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context,25),pendingIntent25);
        } else {
            Log.d("InitAlarmManager", "alarm for 25% was set before. do nothing.");
        }

        if (!alarm50AlreadySet && notif50) {
            Log.d("InitAlarmManager", "alarm for 50% was not set before. Setting 50% alarm");
            pendingIntent50 = PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context,25),pendingIntent75);
        } else {
            Log.d("InitAlarmManager", "alarm for 50% was set before. do nothing.");
        }

        if (!alarm75AlreadySet && notif75) {
            Log.d("InitAlarmManager", "alarm for 75% was not set before. Setting 75% alarm");
            pendingIntent75 = PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillis(context,25),pendingIntent75);
        } else {
            Log.d("InitAlarmManager", "alarm for 75% was set before. do nothing.");
        }
    }

    public static void cancelAlarmManager(Context context) {
        //int prefID[] =
        // todo: cancel only the alarm which preferences are false
        Log.d("InitAlarmManager", "cancelled alarm.");
        PendingIntent.getBroadcast(context.getApplicationContext(), 25, alarmIntent25, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        PendingIntent.getBroadcast(context.getApplicationContext(), 50, alarmIntent50, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        PendingIntent.getBroadcast(context.getApplicationContext(), 75, alarmIntent75, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

    }

    private static long getNextAlarmMillis(Context context, int percent){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + (61*1000));
        //String triggerTime = "20 00";
        Log.d("InitAlarmManager", "triggerTime: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        Log.d("InitAlarmManager", "triggerDate: " + cal.get(Calendar.DAY_OF_MONTH) + ":" + cal.get(Calendar.MONTH));

        //String[] parts = triggerTime.split(" ");
        Log.d("Calendar", "" + cal.get(Calendar.HOUR_OF_DAY) + " . " + cal.get(Calendar.MINUTE));

        return cal.getTimeInMillis();

    }
}
