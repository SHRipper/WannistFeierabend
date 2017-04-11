package de.lukas.wannistfeierabend.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Lukas on 11.04.2017.
 */

public class TimeManager {
    Calendar c = Calendar.getInstance();
    SharedPreferences sharedPreferences;
    public TimeManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        c.setTimeZone(TimeZone.getDefault());
    }
    public int getTimePeriodDone(){
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int startTime = getTimeInMinutesForToday()[0];
        return (c.get(Calendar.HOUR_OF_DAY)*60 + c.get(Calendar.MINUTE)) - startTime;
    }
    public String getRemainingTime(){
       return "";
    }
    public String getPassedTime(){
        c = Calendar.getInstance();
        int time[] = getTimeInMinutesForToday();
        int passed = (c.get(Calendar.HOUR_OF_DAY)*3600
                + c.get(Calendar.MINUTE)*60 + c.get(Calendar.SECOND)) - time[0] * 60;
        int remaining = (getTimePeriodMinutes(time[0], time[1]) *60) - passed;

        Log.d("passed", "" + passed);
        Log.d("remaining", "" + remaining);
        Log.d("compare", (remaining + passed)/60 + " : " +getTimePeriodMinutes(time[0], time[1]));

        int seconds = passed % 60;
        int minutes = (passed /60) % 60;
        int hours = passed / 3600;
        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
    public int getTimePeriodMinutes(int startTime, int endTime){
        return endTime - startTime;
    }
    public int getTimePeriodMinutes(String timePeriod){
        int i[] = splitTimeStringToTimesInMinutes(timePeriod);
        // endtime minus starttime
        return i[1] - i[0];
    }
    public int[] getTimeInMinutesForToday() {
        String time = sharedPreferences.getString(getWeekdayKey(), "8:00 - 13:00");
        return splitTimeStringToTimesInMinutes(time);
    }
    private int[] splitTimeStringToTimesInMinutes(String t){
        String time[] = t.split(" - ");
        String startTime = time[0];
        String endTime = time[1];

        int times[] = new int[2];
        //starttime in minutes
        times[0] = Integer.parseInt(startTime.split(":")[0]) * 60 + Integer.parseInt(startTime.split(":")[1]);
        // endtime in minutes
        times[1] = Integer.parseInt(endTime.split(":")[0]) * 60 + Integer.parseInt(endTime.split(":")[1]);
        return times;
    }

    private String getWeekdayKey() {
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Log.d("", "Day index" + dayOfWeek);

        String weekdays[] = {"key_time_monday", "key_time_tuesday",
                "key_time_wednesday", "key_time_thursday", "key_time_friday"};

        // sunday is 1 and saturday is 7, monday = 2 to friday = 6
        if (dayOfWeek > 6 || dayOfWeek == 1) {
            return "none";
        }
        // monday = 2-2 = 0; friday = 6-2=4
        return weekdays[dayOfWeek - 2];
    }

}

