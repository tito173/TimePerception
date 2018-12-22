package com.timeperseption.TPSmart;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
* Servicio dedicado a encender la clase Service cuando el celular es eencendido o reiniciado.
*/
public class BootService extends BroadcastReceiver {
    final String TAG = "TP-Smart";

    @Override
    public void onReceive(Context context, Intent intent) {

        //enviar los log correspondientes
        Questionnaire.SendTheLogs(context);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "Boot Complete");
            Intent intent1 = new Intent(context, Services.class);
            context.startService(intent1);

            Intent intentService = new Intent(context,MyServiceIsRunning.class);
            context.startService(intentService);

            SetNotification.setAlarmPerseption(context);
            Questionnaire.SendTheLogs(context);



        }





    }
}
