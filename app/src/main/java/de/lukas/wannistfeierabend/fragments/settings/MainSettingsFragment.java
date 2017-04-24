package de.lukas.wannistfeierabend.fragments.settings;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.core.DownloadListener;
import de.lukas.wannistfeierabend.core.MyAlarmManger;
import de.lukas.wannistfeierabend.core.UpdateAlarmManager;
import de.lukas.wannistfeierabend.core.UpdateManager;

public class MainSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        FragmentManager.OnBackStackChangedListener,
        DownloadListener{

    Context context;
    SharedPreferences sharedPreferences;
    Preference notificationPreference, updatePreference,autoUpdateIntervalPreference;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_general);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        updatePreference = findPreference("key_updates_check");
        notificationPreference = findPreference("key_notifications_intervall");
        Preference autoUpdatePreference = findPreference("key_auto_updates_enable");
        autoUpdateIntervalPreference = findPreference("key_update_interval");
        Preference notificationEnable = findPreference("key_notifications_enable");
        Preference schedulePreference = findPreference("key_schedule");
        Preference showSaturdayPreference = findPreference("key_saturday_show");

        notificationEnable.setOnPreferenceClickListener(this);
        notificationPreference.setOnPreferenceClickListener(this);
        updatePreference.setOnPreferenceClickListener(this);
        schedulePreference.setOnPreferenceClickListener(this);
        showSaturdayPreference.setOnPreferenceClickListener(this);
        autoUpdatePreference.setOnPreferenceClickListener(this);

        getFragmentManager().addOnBackStackChangedListener(this);

        setBooleanSummary(notificationEnable,"An", "Aus");
        setBooleanSummary(showSaturdayPreference, "Ja", "Nein");
        setBooleanSummary(autoUpdatePreference, "aktiviert","deaktiviert");

        setIntervallSummary();

        getStorageWritePermission();

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo("de.lukas.wannistfeierabend", 0);
            findPreference("key_version").setSummary("version_" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getStorageWritePermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0;
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    @Override
    public void onFinishedSuccess(final String newVersion) {
        progressDialog.cancel();
        if (newVersion.equals("")) {
            Toast.makeText(getActivity(),"Die App ist auf dem neuesten Stand.",Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final UpdateManager um = new UpdateManager(this, getActivity());
        builder.setMessage("Ein Update auf die Version " + newVersion + " ist vorhanden.\n\nJetzt herunterladen?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Herunterladen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        um.getUpdate(newVersion);
                    }
                })
                .setNegativeButton("Abbrechen",null)
                .create()
                .show();
    }

    @Override
    public void onFinishedFailure() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Fehlende Berechtigung!\nDie App muss auf das Dateisystem zugreifen können.\nÄndere dies in den Einstellungen um Updates zu erhalten.")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok",null)
                .create()
                .show();
    }

    private void setBooleanSummary(Preference pref, String enabled, String disabled){
        if (sharedPreferences.getBoolean(pref.getKey(),false)){
            pref.setSummary(enabled);
        }else{
            pref.setSummary(disabled);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyAlarmManger am = new MyAlarmManger(getActivity());
        am.cancelAllAlarms();
        am.setNextAlarm();

        UpdateAlarmManager updateAlarmManager = new UpdateAlarmManager(getActivity());
        updateAlarmManager.cancelUpdateAlarm();
        updateAlarmManager.setUpdateAlarm();
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
            UpdateManager um = new UpdateManager(this,getActivity());
            um.checkForUpdate();
            progressDialog = new ProgressDialog(getActivity(),R.style.DialogTheme);
            progressDialog.setMessage("Es wird nach einem Update gesucht...");
            progressDialog.show();
        }
        if (preference.getKey().equals("key_auto_updates_enable")){
            setBooleanSummary(preference, "aktiviert","deaktiviert");
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        setIntervallSummary();
    }
}
