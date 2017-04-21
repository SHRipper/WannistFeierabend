package de.lukas.wannistfeierabend.core;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import de.lukas.wannistfeierabend.BuildConfig;
import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Lukas on 12.04.2017.
 */

public class UpdateManager extends AsyncTask<String, Void, Boolean> {

    final String versionURL = "https://www.dropbox.com/s/vmpwripb1pgwllg/version.txt?dl=1";
    final String appURL = "https://www.dropbox.com/s/evlig9jo46u6vjr/Wann%20ist%20Feierabend.apk?dl=1";
    DownloadListener downloadListener;
    String fetchedVersion;
    String thisVersion;
    Context context;


    public UpdateManager(DownloadListener downloadListener, Context context){
        this.context = context;
        this.downloadListener = downloadListener;
        thisVersion = BuildConfig.VERSION_NAME;
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
            downloadListener.onFinishedSuccess("");
        } else {
            downloadListener.onFinishedSuccess(fetchedVersion);
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

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        // Start download
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean){
            downloadListener.onFinishedFailure();
        }
    }

}
