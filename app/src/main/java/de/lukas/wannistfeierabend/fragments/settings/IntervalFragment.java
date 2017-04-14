package de.lukas.wannistfeierabend.fragments.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.core.MyAlarmManger;

/**
 * Created by Lukas on 10.04.2017.
 */

public class IntervalFragment extends PreferenceFragment implements OnPreferenceClickListener {
    Preference notification25;
    Preference notification50;
    Preference notification75;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_interval);
        notification25 = findPreference("key_notifications_25");
        notification50 = findPreference("key_notifications_50");
        notification75 = findPreference("key_notifications_75");

        notification25.setOnPreferenceClickListener(this);
        notification50.setOnPreferenceClickListener(this);
        notification75.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.getBoolean(preference.getKey(),false)){
            MyAlarmManger.setAlarmManager(getActivity());
        }else{
            MyAlarmManger.cancelAlarmManager(getActivity());
        }
        return true;
    }
}
