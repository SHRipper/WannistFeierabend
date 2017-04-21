package de.lukas.wannistfeierabend.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimeManager;

/**
 * Created by Lukas on 05.10.2016.
 */

public class PercentFragment extends Fragment implements FloatingActionButton.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    ProgressBar progressBar;
    TextView textProgress;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;

    int animatorDuration;
    int progressBarMax;
    int progressDone;

    ObjectAnimator objectAnimator;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show percent fragment
        View view = inflater.inflate(R.layout.fragment_percent, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        textProgress = (TextView) view.findViewById(R.id.text_process_state);

        progressBarMax = progressBar.getMax();
        Log.d("PercentFragment", "Progressbar max: " + progressBarMax);
        animatorDuration = getContext().getResources().getInteger(R.integer.animator_duration);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_refresh);
        fab.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        startProgressAnim();
        resetProgress();
    }

    private void startProgressAnim() {
        Log.d("PercentFragment", "startProgressAnim called");

        objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, getPercentDone());
        objectAnimator.setDuration(animatorDuration);
        objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
        objectAnimator.start();

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) objectAnimator.getAnimatedValue();
                textProgress.setText((currentValue / 10) + "%");
                if (currentValue == progressBar.getMax()) {
                    textProgress.setText(textProgress.getText() + "\nGeschafft!");
                }
            }
        });
    }

    private int getPercentDone() {
        TimeManager tm = new TimeManager(getContext());
        int times[] = tm.getTimeInMinutesForToday();
        int timePeriod = tm.getTimePeriodMinutes(times[0],times[1]);
        progressDone = (int) Math.round((tm.getTimePeriodDone() / (double) timePeriod) * 100);
        Log.d("Time", "Period: " + timePeriod);
        Log.d("Time", "Current: " + tm.getTimePeriodDone());
        Log.d("Time", "Done in %: " + progressDone);

        if (progressDone > 100) progressDone = 100;
        if (progressDone < 0) progressDone = 0;
        return progressDone * 10;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && sharedPreferences != null) {
            Log.d("PercentFragment", "PercentFragment was selected");
            startProgressAnim();
        } else {
            try {
                resetProgress();
            } catch (NullPointerException e) {
                Log.d("PercentFragment", "NullPointerException raised during View modification");
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
        Log.d("PercentFragment", "Share FAB clicked.");

        String message = getShareMessage();
        Log.d("PercentFragment","Shared message is " + message);
        if (sharedPreferences.getBoolean("key_downloadlink_show",false)){
            message += "\n\nJetzt die App herunterladen: "
                    + getResources().getString(R.string.dropbox_download_link);
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        try {
            startActivity(sendIntent);
        }catch(ActivityNotFoundException e ){
            Toast.makeText(getActivity(),"Diese Funktion ist ohne Whatsapp nicht verfügbar.",Toast.LENGTH_SHORT).show();
        }

    }

    private String getShareMessage(){
        String messages[] = {
                "Ich habe schon *{progress}%* des Tages geschafft!",
                "Fast geschafft... schon {progress}% erledigt.",
                "Daaaaaaaaaaaaaaaaaaaaaaaaaamn {progress}% geschafft!",
        };

        String finishedMessages[] ={
                "Ich habe Feierabend! {progress}% erledigt.",
                "{progress}% geschafft. Ab nach Hause!"
        };
        Random rnd = new Random();
        String message = "";
        if (progressDone == 100){
            message = finishedMessages[rnd.nextInt(2)];
            for (int i = 0; i< 100;i++){
                Log.d("RndTest","2: " + rnd.nextInt(2));
            }
        }else{
            message = messages[rnd.nextInt(3)];
        }
        String progress = "" + progressDone;

        return message.replace("{progress}",progress);
    }

    private void resetProgress() {
        Log.d("PercentFragment", "reset Progress called.");
        try {
            progressBar.setProgress(0);
            textProgress.setText("0%");
        } catch (NullPointerException e) {
            Log.d("PercentFragment", "NullPointerException raised during View modification");
        }
    }

    @Override
    public void onRefresh() {
        resetProgress();
        startProgressAnim();
        swipeRefreshLayout.setRefreshing(false);
    }
}
