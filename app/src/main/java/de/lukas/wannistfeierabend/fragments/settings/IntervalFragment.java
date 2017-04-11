package de.lukas.wannistfeierabend.fragments.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import de.lukas.wannistfeierabend.R;

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
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        return true;
    }
}
