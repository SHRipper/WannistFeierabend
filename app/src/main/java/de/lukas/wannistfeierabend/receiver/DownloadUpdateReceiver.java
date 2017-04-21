package de.lukas.wannistfeierabend.receiver;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.core.DownloadListener;
import de.lukas.wannistfeierabend.core.UpdateManager;

/**
 * Created by Lukas on 21.04.2017.
 */

public class DownloadUpdateReceiver extends BroadcastReceiver implements DownloadListener {
    UpdateManager um;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DownloadUpdateReceiver", "received something");
        this.context = context;
        Log.d("DownloadUpdateReceiver", "received id: " + intent.getIntExtra("id", -1));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("id",110));

        um = new UpdateManager(this,context);
        um.checkForUpdate();

    }

    @Override
    public void onFinishedSuccess(String newVersion) {
        Log.d("DownloadUpdateReceiver","download new version.");
        um.getUpdate(newVersion);
    }

    @Override
    public void onFinishedFailure() {
        // do nothing
    }
}
