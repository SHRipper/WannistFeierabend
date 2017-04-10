package de.lukas.wannistfeierabend.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import de.lukas.wannistfeierabend.R;

/**
 * Created by Lukas on 05.10.2016.
 */

public class PercentFragment extends Fragment implements FloatingActionButton.OnClickListener{

    ProgressBar progressBar;
    TextView textProgress;
    FloatingActionButton fab;

    int animatorDuration;
    int progressBarMax;

    ObjectAnimator objectAnimator;

    long startTime;
    long endTime;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show percent fragment
        View view = inflater.inflate(R.layout.fragment_percent,container,false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        textProgress = (TextView) view.findViewById(R.id.text_process_state);

        progressBarMax = progressBar.getMax();
        Log.d("PercentFragment","Progressbar max: " + progressBarMax);
        animatorDuration = getContext().getResources().getInteger(R.integer.animator_duration);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_refresh);
        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        startProgressAnim();
        resetProgress();

    }

    private void startProgressAnim(){
        Log.d("PercentFragment","startProgressAnim called");

        objectAnimator = ObjectAnimator.ofInt (progressBar, "progress" , 0, getPercentDone());
        objectAnimator.setDuration (animatorDuration);
        objectAnimator.setInterpolator (new FastOutSlowInInterpolator());
        objectAnimator.start ();

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue =(int) objectAnimator.getAnimatedValue();
                textProgress.setText((currentValue/10) +"%");
                if (currentValue == progressBar.getMax()){
                    textProgress.setText(textProgress.getText()+ "\nGeschafft!");
                }
            }
        });
    }

    private int getPercentDone(){

        String weekday = getWeekdayKey();
        if (weekday.equals("none")){
            return 0;
        }
        String time = sharedPreferences.getString(weekday, "0:00 - 21:00");
        Log.d("Pref time", time);
        int times[] = getTimesInMinutes(weekday);
        int endTime = times[1];
        int startTime = times[0];
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.setTimeZone(TimeZone.getDefault());
        int currentTime = (c.get(Calendar.MINUTE) + c.get(Calendar.HOUR_OF_DAY)* 60) - startTime;
        int timePeriod = (endTime - startTime);
        int percentageDone = (int) Math.round((currentTime / (double)timePeriod) * 100);
        Log.d("Time","" + timePeriod);
        Log.d("Time","" + currentTime);
        Log.d("Time","" +  percentageDone);

        if (percentageDone > 100){
            return 100 * 10;
        }
        return percentageDone * 10;
    }
    private int[] getTimesInMinutes(String key){
        int times[] = new int[2];
        String time[] = sharedPreferences.getString(key,"8:00 - 13:00").split(" - ");

        String startTime = time[0];
        String endTime = time[1];

        times[0] = Integer.parseInt(startTime.split(":")[0])* 60 + Integer.parseInt(startTime.split(":")[1]);
        times[1] = Integer.parseInt(endTime.split(":")[0])*60+Integer.parseInt(endTime.split(":")[1]);
        return times;
    }
    private String getWeekdayKey(){
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Log.d("","Day index" + dayOfWeek);

        String weekdays[] = {"key_time_monday", "key_time_tuesday",
        "key_time_wednesday", "key_time_thursday", "key_time_friday"};

        // sunday is 1 and saturday is 7, monday = 2 to friday = 6
        if (dayOfWeek > 6 || dayOfWeek == 1){
            return "none";
        }
        // monday = 2-2 = 0; friday = 6-2=4
        return weekdays[dayOfWeek-2];
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && sharedPreferences != null){
            Log.d("PercentFragment", "PercentFragment was selected");
            startProgressAnim();
        }
        else {
            try{
                progressBar.setProgress(0);
                textProgress.setText("0%");
            }catch (NullPointerException e){
                Log.d("PercentFragment","NullPointerException raised during View modification");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startProgressAnim();
    }

    @Override
    public void onPause() {
        super.onPause();
        resetProgress();

    }

    @Override
    public void onClick(View view) {
        Log.d("PercentFragment","Reset FAB clicked.");
        // FAB onClick to refresh the animation process
        if (!objectAnimator.isRunning()){
            resetProgress();
            startProgressAnim();
        }
    }

    private void resetProgress(){
        Log.d("PercentFragment","reset Progress called.");
        try{
            progressBar.setProgress(0);
            textProgress.setText("0%");
        }catch (NullPointerException e){
            Log.d("PercentFragment","NullPointerException raised during View modification");
        }
    }
}
