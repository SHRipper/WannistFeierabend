package de.lukas.wannistfeierabend.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import de.lukas.wannistfeierabend.activities.MainActivity;
import de.lukas.wannistfeierabend.core.MyVersionChecker;
import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;

/**
 * Created by Lukas on 12.04.2017.
 */

public class UpdateManager {

    String url = "https://www.dropbox.com/s/iwal8w8h531kcn7/test.txt?dl=1";
    MainSettingsFragment settingsFragment;
    SharedPreferences sp;
    boolean init = false;

    public UpdateManager(MainSettingsFragment sf){
        this.settingsFragment = sf;
        sp = sf.getPreferenceManager().getSharedPreferences();
    }
    public UpdateManager(MainSettingsFragment sf, boolean initial){
        this.settingsFragment = sf;
        init = initial;
        sp = sf.getPreferenceManager().getSharedPreferences();

    }

    public void checkForUpdate(){
        new MyVersionChecker(this, url,init);
    }

    public void setFetchedVersion(String fetchedVersion){
        sp = settingsFragment.getPreferenceManager().getSharedPreferences();
        if (fetchedVersion.startsWith("init_")){
            sp.edit().putString("key_version", fetchedVersion).apply();
            settingsFragment.findPreference("key_version").setSummary(fetchedVersion);
        }else{
            compareVersions(fetchedVersion);
        }
    }

    private void compareVersions(String fetchedVersion){
        sp = settingsFragment.getPreferenceManager().getSharedPreferences();
        Log.d("key_version", sp.getString("key_version","default"));
        if (fetchedVersion.equals(sp.getString("key_version", "version_0.0"))){
            Log.d("update","no update");
            settingsFragment.noUpdate();
        }else{
            sp.edit().putString("key_version", fetchedVersion).apply();
            Log.d("update","got update");
            settingsFragment.updateFound(fetchedVersion);
        }
    }
}
