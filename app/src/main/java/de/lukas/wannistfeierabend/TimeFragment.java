package de.lukas.wannistfeierabend;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by Lukas on 05.10.2016.
 */

public class TimeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        // custom actions
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setProgress(10
        );

        return view;
    }
}
