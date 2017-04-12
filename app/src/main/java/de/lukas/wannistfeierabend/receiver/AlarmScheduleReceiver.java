package de.lukas.wannistfeierabend.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.lukas.wannistfeierabend.core.NotificationBuilder;

/**
 * Created by Lukas on 08.05.2016.
 */
public class AlarmScheduleReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyAlarmSchedulerReceive", "show notification");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, NotificationBuilder.build(context));
    }
}