package de.lukas.wannistfeierabend.core;

import android.Manifest;
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Lukas on 12.04.2017.
 */

public class UpdateManager extends AsyncTask<String, Void, Boolean> {

    final String versionURL = "https://www.dropbox.com/s/vmpwripb1pgwllg/version.txt?dl=1";
    final String appURL = "https://www.dropbox.com/s/evlig9jo46u6vjr/Wann%20ist%20Feierabend.apk?dl=1";
    MainSettingsFragment settingsFragment;
    String fetchedVersion;
    String thisVersion;


    public UpdateManager(MainSettingsFragment sf){
        this.settingsFragment = sf;
        try {
            thisVersion = sf.getActivity().getPackageManager().getPackageInfo("de.lukas.wannistfeierabend",0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getUpdate(String newVersion) {
        this.execute(newVersion);
    }

    public void checkForUpdate() {
        new MyVersionChecker(this, versionURL);
    }

    public void setFetchedVersion(String fetchedVersion) {
        this.fetchedVersion = fetchedVersion;
        compareVersions();
    }

    private void compareVersions() {
        Log.d("this version", thisVersion);
        if (fetchedVersion.equals(thisVersion)) {
            Log.d("update", "no update");
            settingsFragment.noUpdate();
        } else {
            settingsFragment.updateFound(fetchedVersion);
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(appURL));
        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Wann ist Feierabend " + strings[0]);
        // Notify user when download is completed
        // (Seems to be available since Honeycomb only)
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        if (ContextCompat.checkSelfPermission(settingsFragment.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        // Start download
        DownloadManager dm = (DownloadManager) settingsFragment.getActivity().getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean){
            settingsFragment.showNoPermissionDialog();
        }
    }

}
