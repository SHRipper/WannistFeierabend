package de.lukas.wannistfeierabend.receiver;

import de.lukas.wannistfeierabend.util.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

        Notification notification = new Notification();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.buildPercentNotification(context,id));

        MyAlarmManger am = new MyAlarmManger(context);
        am.cancelAllAlarms();
        am.setNextAlarm();
    }


}