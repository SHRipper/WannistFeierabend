package de.lukas.wannistfeierabend.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import de.lukas.wannistfeierabend.R;

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
       // if (preference.getKey().equals("key_notification_time"))
        return false;
    }
}
