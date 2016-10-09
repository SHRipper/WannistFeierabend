package de.lukas.wannistfeierabend;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Lukas on 05.10.2016.
 */

public class PercentFragment extends Fragment {

    ProgressBar progressBar;
    TextView textProgress;

    int animatorDuration;
    int progressBarMax;

    long startTime;
    long endTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show percent fragment
        View view = inflater.inflate(R.layout.fragment_percent,container,false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        textProgress = (TextView) view.findViewById(R.id.text_process_state);

        progressBarMax = progressBar.getMax() +1;
        animatorDuration = getContext().getResources().getInteger(R.integer.animator_duration);

        startProgressAnim();

        return view;
    }

    private void startProgressAnim(){
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, getPercentDone()+1);
        animation.setDuration (animatorDuration);
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();

        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = progressBar.getProgress();

                textProgress.setText(currentValue +"%");
                if (currentValue == 100){
                    textProgress.setText(textProgress.getText()+ "\nGeschafft!");
                }
            }
        });
    }

    private int getPercentDone(){
        Calendar calendar = Calendar.getInstance();

        long currentTime = calendar.getTimeInMillis();

        calendar.set(2016,Calendar.OCTOBER,9,10,0,0);
        long startTime = calendar.getTimeInMillis();

        calendar.set(2016,Calendar.OCTOBER,9,20,0,0);
        long endTime = calendar.getTimeInMillis();

        long entirePeriod = (endTime - startTime) / 1000;
        long timeDone = (currentTime - startTime) / 1000;

        int percentDone = (int) Math.ceil((timeDone*100) / entirePeriod);
        return percentDone;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            Log.d("TimeFragment", "TimeFragement was selected");

            startProgressAnim();
        }
        else {
            try{
                progressBar.setProgress(0);
                textProgress.setText("0%");
            }catch (NullPointerException e){
                Log.d("TimeFragment","NullPointerException raised during View modification");
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
        try{
            progressBar.setProgress(0);
            textProgress.setText("0%");
        }catch (NullPointerException e){
            Log.d("TimeFragment","NullPointerException raised during View modification");
        }

    }
}
