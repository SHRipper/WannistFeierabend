package de.lukas.wannistfeierabend.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimePreferenceDialog;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    Context context;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Preference notificationPreference = findPreference("key_notifications_time");
        Preference schedulePreference = findPreference("key_schedule");
        notificationPreference.setOnPreferenceClickListener(this);
        schedulePreference.setOnPreferenceClickListener(this);

        notificationPreference.setSummary(sharedPreferences.getString("key_notifications_time", "8:00"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("key_notifications_time")){
            Log.d("MainSettingsFragment", "preference clicked: " + preference.getKey());
            new TimePreferenceDialog(preference, this.context).showDialog();
        }
        if (preference.getKey().equals("key_schedule")){
            Log.d("MainSettingsFragment", "create schedule was clicked.");
            getFragmentManager().beginTransaction()
                    .addToBackStack("main_settings")
                    .replace(R.id.settings_fragment, new ScheduleFragment())
                    .commit();
        }
        return true;
    }
}
