package de.lukas.wannistfeierabend.fragments.settings;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.util.UpdateManager;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, FragmentManager.OnBackStackChangedListener{

    Context context;
    SharedPreferences sharedPreferences;
    Preference notificationPreference, updatePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        updatePreference = findPreference("key_updates_check");
        notificationPreference = findPreference("key_notifications_intervall");
        Preference notificationEnable = findPreference("key_notifications_enable");
        Preference schedulePreference = findPreference("key_schedule");
        Preference showSaturdayPreference = findPreference("key_saturday_show");

        notificationEnable.setOnPreferenceClickListener(this);
        notificationPreference.setOnPreferenceClickListener(this);
        updatePreference.setOnPreferenceClickListener(this);
        schedulePreference.setOnPreferenceClickListener(this);
        showSaturdayPreference.setOnPreferenceClickListener(this);

        getFragmentManager().addOnBackStackChangedListener(this);

        setBooleanSummary(notificationEnable,"An", "Aus");
        setBooleanSummary(showSaturdayPreference, "Ja", "Nein");

        setIntervallSummary();

        sharedPreferences = getPreferenceManager().getSharedPreferences();
        if ("version_0.0".equals(sharedPreferences.getString("key_version", "version_0.0"))){
            new UpdateManager(this,true).checkForUpdate();
        }
        findPreference("key_version").setSummary(sharedPreferences.getString("key_version", "default"));


    }

    public void noUpdate(){
        Toast.makeText(getActivity(),"Die App ist auf dem neuesten Stand.",Toast.LENGTH_SHORT).show();
    }

    public void updateFound(String newVersion){
        Toast.makeText(getActivity(),"ein update wurde gefunden: " + newVersion,Toast.LENGTH_SHORT).show();

    }

    private void setBooleanSummary(Preference pref, String enabled, String disabled){
        if (sharedPreferences.getBoolean(pref.getKey(),false)){
            pref.setSummary(enabled);
        }else{
            pref.setSummary(disabled);
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
            setBooleanSummary(preference, "An", "Aus");
        }
        if (preference.getKey().equals("key_saturday_show")){
            setBooleanSummary(preference, "Ja", "Nein");
        }
        if (preference.getKey().equals("key_updates_check")){
            UpdateManager um = new UpdateManager(this);
            um.checkForUpdate();
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        setIntervallSummary();
    }
}
