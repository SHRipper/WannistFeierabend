package de.lukas.wannistfeierabend.fragments.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.core.TimeIntervalPreferenceDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    SharedPreferences sharedPreferences;
    Context context;

    Preference prefMonday;
    Preference prefTuesday;
    Preference prefWednesday;
    Preference prefThursday;
    Preference prefFriday;
    Preference prefSaturday;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_schedule);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        prefMonday = findPreference("key_time_monday");
        prefMonday.setOnPreferenceClickListener(this);
        prefTuesday = findPreference("key_time_tuesday");
        prefTuesday.setOnPreferenceClickListener(this);
        prefWednesday = findPreference("key_time_wednesday");
        prefWednesday.setOnPreferenceClickListener(this);
        prefThursday = findPreference("key_time_thursday");
        prefThursday.setOnPreferenceClickListener(this);
        prefFriday = findPreference("key_time_friday");
        prefFriday.setOnPreferenceClickListener(this);
        prefSaturday = findPreference("key_time_saturday");

        if (sharedPreferences.getBoolean("key_saturday_show",false)){
            prefSaturday.setOnPreferenceClickListener(this);
            prefSaturday.setEnabled(true);
        }else{
            prefSaturday.setEnabled(false);
        }

        setPrefSummaries();
    }
    private void setPrefSummaries(){
        String DEFAULT = getString(R.string.default_time);
        prefMonday.setSummary(sharedPreferences.getString("key_time_monday", DEFAULT));
        prefTuesday.setSummary(sharedPreferences.getString("key_time_tuesday", DEFAULT));
        prefWednesday.setSummary(sharedPreferences.getString("key_time_wednesday", DEFAULT));
        prefThursday.setSummary(sharedPreferences.getString("key_time_thursday", DEFAULT));
        prefFriday.setSummary(sharedPreferences.getString("key_time_friday", DEFAULT));
        prefSaturday.setSummary(sharedPreferences.getString("key_time_saturday", DEFAULT));


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        new TimeIntervalPreferenceDialog(preference,context).showDialog();
        return true;
    }
}
