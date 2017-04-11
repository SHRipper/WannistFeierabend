package de.lukas.wannistfeierabend.fragments.settings;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import de.lukas.wannistfeierabend.R;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, FragmentManager.OnBackStackChangedListener{

    Context context;
    SharedPreferences sharedPreferences;
    Preference notificationPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        notificationPreference = findPreference("key_notifications_intervall");
        Preference notificationEnable = findPreference("key_notifications_enable");
        Preference schedulePreference = findPreference("key_schedule");
        notificationEnable.setOnPreferenceClickListener(this);
        notificationPreference.setOnPreferenceClickListener(this);
        schedulePreference.setOnPreferenceClickListener(this);
        setBooleanSummary(notificationEnable);
        setIntervallSummary();
        getFragmentManager().addOnBackStackChangedListener(this);



    }

    private void setBooleanSummary(Preference pref){
        if (sharedPreferences.getBoolean(pref.getKey(),false)){
            pref.setSummary("An");
        }else{
            pref.setSummary("Aus");
        }
    }

    private void setIntervallSummary(){
        String summary = "";
        if (sharedPreferences.getBoolean("key_notifications_25", false)){
            summary += "25%";
        }
        if (sharedPreferences.getBoolean("key_notifications_50", false)){
            summary += "50%";
        }
        if (sharedPreferences.getBoolean("key_notifications_75", false)){
            summary += "75%";
        }
        Log.d("MainSettings",summary);
        if (summary.length() != 0) {
            summary = summary.replace("%", "%, ");
            summary = summary.substring(0, summary.length() - 2);
            notificationPreference.setSummary(summary);
        }else{
            notificationPreference.setSummary("Nie");
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("key_notifications_intervall")) {
            Log.d("MainSettingsFragment", "preference clicked: " + preference.getKey());
            getFragmentManager().beginTransaction()
                    .addToBackStack("main_settings")
                    .replace(R.id.settings_fragment, new IntervalFragment())
                    .commit();
        }
        if (preference.getKey().equals("key_schedule")) {
            Log.d("MainSettingsFragment", "create schedule was clicked.");
            getFragmentManager().beginTransaction()
                    .addToBackStack("main_settings")
                    .replace(R.id.settings_fragment, new ScheduleFragment())
                    .commit();
        }
        if (preference.getKey().equals("key_notifications_enable")) {
            setBooleanSummary(preference);
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        setIntervallSummary();
    }
}
