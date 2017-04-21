package de.lukas.wannistfeierabend.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.activities.MainActivity;
import de.lukas.wannistfeierabend.core.DownloadListener;
import de.lukas.wannistfeierabend.core.UpdateManager;
import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;

/**
 * Created by Lukas on 16.04.2017.
 */

public class UpdateAlarmReceiver extends BroadcastReceiver implements DownloadListener{
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        UpdateManager um = new UpdateManager(this, context);
        um.checkForUpdate();
    }

    @Override
    public void onFinishedSuccess(String newVersion) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(110, buildNotification(context));
    }

    @Override
    public void onFinishedFailure() {
        // do nothing
    }

    private Notification buildNotification(Context context){
        int id = 110;
        Intent intent = new Intent(context, DownloadUpdateReceiver.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Feierabend?");
        builder.setContentText("Ein neues Update ist vorhanden!");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.addAction(0, "Herunterladen",pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        return builder.build();
    }
}
