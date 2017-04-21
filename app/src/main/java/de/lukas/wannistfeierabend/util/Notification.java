package de.lukas.wannistfeierabend.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.activities.MainActivity;
import de.lukas.wannistfeierabend.fragments.settings.MainSettingsFragment;

/**
 * Created by Lukas on 18.04.2017.
 */

public class Notification {

    public android.app.Notification buildPercentNotification(Context context, int percent){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Feierabend?");
        builder.setContentText(percent + "% geschafft!");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(android.app.Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        return builder.build();
    }

    public android.app.Notification buildUpdateNotification(Context context){
        Intent intent = new Intent(context, MainSettingsFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Feierabend?");
        builder.setContentText("Ein neues Update ist verfügbar!");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(android.app.Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        return builder.build();
    }
}
