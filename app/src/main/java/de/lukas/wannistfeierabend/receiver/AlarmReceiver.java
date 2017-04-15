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
import android.util.Log;

import de.lukas.wannistfeierabend.R;
import de.lukas.wannistfeierabend.activities.MainActivity;
import de.lukas.wannistfeierabend.core.MyAlarmManger;

/**
 * Created by Lukas on 08.05.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyAlarmSchedulerReceive", "show notification");
        int id = intent.getIntExtra("id",0);
        Log.d("AlarmReceiver", "Extra from intent: " + id);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, buildNotification(context,id));

        MyAlarmManger am = new MyAlarmManger(context);
        am.cancelAllAlarms();
        am.setNextAlarm();
    }

    private Notification buildNotification(Context context, int percent){
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Feierabend?");
        builder.setContentText(percent + "% geschafft!");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        //builder.addAction(R.drawable.icon_share_24dp, "Auf Whatsapp teilen",sendIntent);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);

        return builder.build();
    }
}