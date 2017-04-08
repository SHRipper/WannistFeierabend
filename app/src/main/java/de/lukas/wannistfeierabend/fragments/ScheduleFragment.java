package de.lukas.wannistfeierabend.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.TimeIntervalPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    SharedPreferences sharedPreferences;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_schedule);

        Preference prefMonday = findPreference("key_time_monday");
        prefMonday.setOnPreferenceClickListener(this);
        Preference prefTuesday = findPreference("key_time_tuesday");
        prefTuesday.setOnPreferenceClickListener(this);
        Preference prefWednesday = findPreference("key_time_wednesday");
        prefWednesday.setOnPreferenceClickListener(this);
        Preference prefThursday = findPreference("key_time_thursday");
        prefThursday.setOnPreferenceClickListener(this);
        Preference prefFriday = findPreference("key_time_friday");
        prefFriday.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        new TimeIntervalPreference(preference,context);
        return true;
    }
}
