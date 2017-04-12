package de.lukas.wannistfeierabend.core;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import de.lukas.wannistfeierabend.activities.MainActivity;
import de.lukas.wannistfeierabend.R;

/**
 * Created by Tim on 08.05.2016.
 */
public class NotificationBuilder {
    public static Notification build(Context context) {
        Intent mIntent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Reise nach Amerika");
        builder.setContentText("noch  Tage");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);

        return builder.build();
    }
}
