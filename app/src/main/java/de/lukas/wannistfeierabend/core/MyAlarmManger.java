package de.lukas.wannistfeierabend.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import de.lukas.wannistfeierabend.R;
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
    boolean notif25, notif50, notif75;
    SharedPreferences sharedPreferences;
    TimeManager tm;
    Context context;
    Calendar c;
    String defaultTime;



    public MyAlarmManger(Context context) {
        this.context = context.getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        tm = new TimeManager(context);
        defaultTime = context.getResources().getString(R.string.default_time);

        Log.d("AlarmManager", "initializing alarm manager");

        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmIntent25 = new Intent(context, AlarmReceiver.class);
        alarmIntent25.putExtra("id", 25);
        alarmIntent50 = new Intent(context, AlarmReceiver.class);
        alarmIntent50.putExtra("id", 50);
        alarmIntent75 = new Intent(context, AlarmReceiver.class);
        alarmIntent75.putExtra("id", 75);

        notif25 = sharedPreferences.getBoolean("key_notifications_25", false);
        notif50 = sharedPreferences.getBoolean("key_notifications_50", false);
        notif75 = sharedPreferences.getBoolean("key_notifications_75", false);

    }

    public void setNextAlarm() {
        int percent = tm.getPercentDone();
        Log.d("AlarmManager", "percent done: " + percent);

        if (notif25 && notif50 && notif75) {
            if (percent >= 25 && percent < 50) {
                setAlarm50();
                return;
            } else if (percent >= 50 && percent < 75) {
                setAlarm75();
                return;
            } else {
                setAlarm25();
                return;
            }
        }
        if (notif25 && notif50) {
            if (percent >= 25 && percent < 50) {
                setAlarm50();
                return;
            } else {
                setAlarm25();
                return;
            }
        }
        if (notif50 && notif75) {

            if (percent >= 50 && percent < 75) {
                setAlarm75();
                return;
            } else {
                setAlarm50();
                return;
            }
        }
        if (notif25 && notif75) {
            if (percent >= 25 && percent < 75) {
                setAlarm75();
                return;
            } else {
                setAlarm25();
                return;
            }
        }

        if (notif25) setAlarm25();
        if (notif50) setAlarm50();
        if (notif75) setAlarm75();

    }

    public void setAlarm25() {
        boolean alarm25AlreadySet = (PendingIntent.getBroadcast(context, 25, alarmIntent25, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm25AlreadySet && notif25) {
            Log.d("AlarmManager", "alarm for 25% was not set before. Setting 25% alarm");
            pendingIntent25 = PendingIntent.getBroadcast(context, 25, alarmIntent25, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(25), pendingIntent25);
        } else {
            if (!notif25) {
                Log.d("AlarmManager", "nofification for 25% is not enabled");
            } else if (alarm25AlreadySet) {
                Log.d("AlarmManager", "alarm for 25% was set before.");
            }
        }
    }

    public void setAlarm50() {

        boolean alarm50AlreadySet = (PendingIntent.getBroadcast(context, 50, alarmIntent50, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm50AlreadySet && notif50) {
            Log.d("AlarmManager", "setting 50% alarm");
            pendingIntent50 = PendingIntent.getBroadcast(context, 50, alarmIntent50, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(50), pendingIntent50);
        } else {
            if (!notif50) {
                Log.d("AlarmManager", "nofification for 50% is not enabled");
            } else if (alarm50AlreadySet) {
                Log.d("AlarmManager", "alarm for 50% was set before.");
            }
        }
    }

    private void setAlarm75() {

        boolean notif75 = sharedPreferences.getBoolean("key_notifications_75", false);
        boolean alarm75AlreadySet = (PendingIntent.getBroadcast(context, 75, alarmIntent75, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarm75AlreadySet && notif75) {
            Log.d("AlarmManager", "alarm for 75% was not set before. Setting 75% alarm");
            pendingIntent75 = PendingIntent.getBroadcast(context, 75, alarmIntent75, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmMillisForPercent(75), pendingIntent75);
        } else {
            if (!notif75) {
                Log.d("AlarmManager", "nofification for 75% is not enabled");
            } else if (alarm75AlreadySet) {
                Log.d("AlarmManager", "alarm for 75% was set before.");
            }
        }
    }

    public void cancelAllAlarms() {
        PendingIntent.getBroadcast(context, 25, alarmIntent25, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        Log.d("AlarmManager", "cancelled alarm 25%.");
        PendingIntent.getBroadcast(context, 50, alarmIntent50, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        Log.d("AlarmManager", "cancelled alarm 50%.");
        PendingIntent.getBroadcast(context, 75, alarmIntent75, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
        Log.d("AlarmManager", "cancelled alarm 75%.");
    }

    private long getNextAlarmMillisForPercent(int percent) {
        int times[] = tm.getTimeInMinutesForToday();
        int startTime = times[0];
        int period = tm.getTotalPeriodForToday();
        boolean showSaturday = sharedPreferences.getBoolean("key_saturday_show", false);
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        int nextAlarmTimeMinutes = (startTime + (int) ((percent / 100.0) * period));
        c.set(Calendar.HOUR_OF_DAY, (nextAlarmTimeMinutes / 60));
        c.set(Calendar.MINUTE, (nextAlarmTimeMinutes % 60));
        c.set(Calendar.SECOND,0);

        // calculate to make comparison possible
        Log.d("AlarmManager", "triggerTime: " + nextAlarmTimeMinutes / 60 +
                ":" + nextAlarmTimeMinutes % 60);
        Log.d("AlarmManager", String.format("%d - %d", c.getTimeInMillis(), System.currentTimeMillis()));

        if (c.getTimeInMillis() <= System.currentTimeMillis()) {
            // The alarm time is scheduled in the past. This is a sign that the
            // next alarm time should be on the next possible day.
            long nextAlarmTimeMillis = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && !showSaturday
                    || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                // The alarm is scheduled on monday on following occasions
                // - it is friday and saturday is not enabled
                // - it is saturday
                nextAlarmTimeMillis = setOnMonday(percent, c.get(Calendar.DAY_OF_WEEK));
            } else {
                // The alarm is scheduled on the next day on following occasions
                // - it is Monday - Thursday
                // - it is Friday with Saturday being enabled
                nextAlarmTimeMillis = setTomorrow(percent);
            }
            return nextAlarmTimeMillis;
        }
        Log.d("AlarmManager", "scheduled time: " + c.getTime());

        return c.getTimeInMillis();
    }

    /**
     * Set the alarm on monday to the time calculated from the percent value.
     *
     * @param percent
     * @return
     */
    private long setOnMonday(int percent, int dayOfWeek) {
        String key = tm.getWeekdayKeyFor(TimeManager.Day.MONDAY);
        int times[] = tm.splitTimeStringToTimesInMinutes(sharedPreferences.getString(key, defaultTime));
        int period = times[1] - times[0];
        c.setTimeInMillis(System.currentTimeMillis());

        if (dayOfWeek == Calendar.FRIDAY) {
            // friday + 3 is monday
            c.add(Calendar.DAY_OF_MONTH, 3);
        } else {
            // dayofweek == Calendar.Saturday
            c.add(Calendar.DAY_OF_MONTH, 2);
        }
        c.set(Calendar.HOUR_OF_DAY, (times[0] + (int) ((percent / 100.0) * period)) / 60);
        c.set(Calendar.MINUTE, (times[0] + (int) ((percent / 100.0) * period)) % 60);
        c.set(Calendar.SECOND,0);

        Log.d("AlarmManager", "time set monday: " + c.getTime());
        return c.getTimeInMillis();
    }

    private long setTomorrow(int percent) {
        String key = tm.getWeekdayKeyFor(TimeManager.Day.TOMORROW);
        int times[] = tm.splitTimeStringToTimesInMinutes(sharedPreferences.getString(key, defaultTime));
        int period = times[1] - times[0];

        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, (times[0] + (int) ((percent / 100.0) * period)) / 60);
        c.set(Calendar.MINUTE, (times[0] + (int) ((percent / 100.0) * period)) % 60);
        c.set(Calendar.SECOND,0);

        Log.d("AlarmManager", "time set tomorrow: " + c.getTime());
        return c.getTimeInMillis();
    }


}
