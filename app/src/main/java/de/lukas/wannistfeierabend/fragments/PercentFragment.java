package de.lukas.wannistfeierabend.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

        startProgressAnim();
        resetProgress();

        return view;
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
        return 80 *10;
//        Calendar calendar = Calendar.getInstance();
//
//        long currentTime = calendar.getTimeInMillis();
//
//        calendar.set(2016,Calendar.OCTOBER,9,10,0,0);
//        long startTime = calendar.getTimeInMillis();
//
//        calendar.set(2016,Calendar.OCTOBER,9,20,0,0);
//        long endTime = calendar.getTimeInMillis();
//
//        long entirePeriod = (endTime - startTime) / 1000;
//        long timeDone = (currentTime - startTime) / 1000;
//
//        int percentDone = (int) Math.ceil((timeDone*100) / entirePeriod);
//        return percentDone;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
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
