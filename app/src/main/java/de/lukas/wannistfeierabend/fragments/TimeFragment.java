package de.lukas.wannistfeierabend.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimeManager;

/**
 * Created by Lukas on 05.10.2016.
 */

public class TimeFragment extends Fragment {
    TextView txtPassed, txtRemaining, txtPassedHeader, txtRemainingHeader;
    Handler handler = new Handler();
    TimeManager tm;

    int delay = 1000;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            startHandler();
        }else{
            stopHandler();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        startHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        txtPassed = (TextView) view.findViewById(R.id.txtPassed);
        txtRemaining = (TextView) view.findViewById(R.id.txtRemaining);
        txtPassedHeader = (TextView) view.findViewById(R.id.txtPassedHeader);
        txtRemainingHeader = (TextView) view.findViewById(R.id.txtRemainingHeader);

        tm = new TimeManager(getActivity());
        String times[] = tm.getClockTimesforToday();
        txtPassedHeader.setText("Vergangene Zeit seit " + times[0] + " Uhr");
        txtRemainingHeader.setText("Verbleibende Zeit bis " + times[1] + " Uhr");

        setTimer();
        return view;
    }

    private void startHandler() {
        handler.post(timerRunnable);
    }

    private void stopHandler() {
        handler.removeCallbacks(timerRunnable);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,delay);
            setTimer();
        }
    };

    private void setTimer(){

        if (txtPassed != null && txtRemaining != null){
            txtPassed.setText(tm.getPassedTime());
            txtRemaining.setText(tm.getRemainingTime());

        }
    }

}
