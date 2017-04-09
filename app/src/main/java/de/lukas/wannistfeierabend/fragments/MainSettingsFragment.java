package de.lukas.wannistfeierabend.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.TimePreferenceDialog;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);

        Preference notificationPreference = findPreference("key_notifications_time");
        Preference schedulePreference = findPreference("key_schedule");
        notificationPreference.setOnPreferenceClickListener(this);
        schedulePreference.setOnPreferenceClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("key_notifications_time")){
            Log.d("MainSettingsFragment", "i dont know yet was clicked");
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
