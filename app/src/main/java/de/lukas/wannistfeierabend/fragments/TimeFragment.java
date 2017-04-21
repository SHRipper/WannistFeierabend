package de.lukas.wannistfeierabend.fragments;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimeManager;

/**
 * Created by Lukas on 05.10.2016.
 */

public class TimeFragment extends Fragment {
    TextView txtPassed, txtRemaining, txtPassedHeader, txtRemainingHeader;
    Handler handler = new Handler();
    TimeManager tm;
    boolean fragmentVisible = false;

    int delay = 1000;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisible = isVisibleToUser;
        if (isVisibleToUser){
            Log.d("Handler","start onVisible");
            startHandler();
            updateHeaders();
        }else{
            Log.d("Handler","stop onHide");
            stopHandler();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Handler","stop onPause");
        stopHandler();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentVisible) {
            Log.d("Handler", "start onResume");
            startHandler();
            updateHeaders();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        txtPassed = (TextView) view.findViewById(R.id.txtPassed);
        txtRemaining = (TextView) view.findViewById(R.id.txtRemaining);
        txtPassedHeader = (TextView) view.findViewById(R.id.txtPassedHeader);
        txtRemainingHeader = (TextView) view.findViewById(R.id.txtRemainingHeader);

        updateHeaders();
        setTimer();
        return view;
    }

    private void updateHeaders(){
        tm = new TimeManager(getActivity());
        String times[] = tm.getClockTimesforToday();
        txtPassedHeader.setText("Vergangene Zeit seit " + times[0] + " Uhr");
        txtRemainingHeader.setText("Verbleibende Zeit bis " + times[1] + " Uhr");
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
