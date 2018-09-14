package tito1.example.com.timeperception;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

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

    }
//    public void SendTheLogs(Context context)  {
//        long minuto = 1000 * 60;
//        long hora = minuto * 60;
//        long dia = hora * 24;
//
//        //create new calendar instance
//        Log.d(TAG,"BoostService SendTheLogs");
//        Calendar SendfileStart = Calendar.getInstance();
//
//        //set the time to midnight tonight
//        SendfileStart.set(Calendar.HOUR_OF_DAY, 0);
//        SendfileStart.set(Calendar.MINUTE,0);
//        SendfileStart.set(Calendar.SECOND, 0);
//
//        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//
//        //create a pending intent to be called at midnight
//        Intent intent1 = new Intent(context,SendFile.class);
//        PendingIntent midnightPI =  PendingIntent.getBroadcast(context,0,intent1,0);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), hora, midnightPI);
//        SharedPreferences mensaje = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
//        mensaje.edit().putString("last","Boost "+Calendar.getInstance().getTime().toString()).commit();

//    }


}
