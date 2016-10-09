package de.lukas.wannistfeierabend;

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

/**
 * Created by Lukas on 05.10.2016.
 */

public class PercentFragment extends Fragment {

    ProgressBar progressBar;
    TextView textProgress;
    int animatorDuration;
    int progressBarMax;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show percent fragment
        View view = inflater.inflate(R.layout.fragment_percent,container,false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        textProgress = (TextView) view.findViewById(R.id.text_process_state);

        animatorDuration = getContext().getResources().getInteger(R.integer.animator_duration);
        progressBarMax = 100; //for style reasons different than progressBar.getMax()

        startProgressAnim();

        return view;
    }

    private void startProgressAnim(){
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, progressBarMax +1); // value goes from 0 to 100
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
}
