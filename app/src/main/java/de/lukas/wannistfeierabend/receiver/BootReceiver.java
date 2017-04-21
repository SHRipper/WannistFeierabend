package de.lukas.wannistfeierabend.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.lukas.wannistfeierabend.core.MyAlarmManger;
import de.lukas.wannistfeierabend.core.UpdateAlarmManager;

/**
 * Created by Tim on 08.05.2016.
 * * see https://developer.android.com/training/scheduling/alarms.html#boot
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReciever", "boot completed");
        MyAlarmManger am = new MyAlarmManger(context);
        am.setNextAlarm();

        UpdateAlarmManager um = new UpdateAlarmManager(context);
        um.setUpdateAlarm();
    }
}