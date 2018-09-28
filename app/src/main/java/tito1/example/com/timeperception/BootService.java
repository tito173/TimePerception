package tito1.example.com.timeperception;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Set;

import static android.content.Context.ALARM_SERVICE;

/*
* Servicio dedicado a encender la clase Service cuando el celular es eencendido o reiniciado.
*/
public class BootService extends BroadcastReceiver {
    final String TAG = "TP-Smart";

    @Override
    public void onReceive(Context context, Intent intent) {

        Questionnaire.SendTheLogs(context);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Boot Complete");
            Intent intent1 = new Intent(context, Services.class);
            context.startService(intent1);

        }

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Alarm reset for PerseptionTest");
//            SetNotification.setAlarmPerseption(context);
//            SetNotificationFinal.setAlarmFinalQuestionnaire(context);

        }



    }
}
