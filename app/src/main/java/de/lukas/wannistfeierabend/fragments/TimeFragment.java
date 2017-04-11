package de.lukas.wannistfeierabend.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimeManager;

/**
 * Created by Lukas on 05.10.2016.
 */

public class TimeFragment extends Fragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("formatted time",new TimeManager(getActivity()).getPassedTime());

    }
}
