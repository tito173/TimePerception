package tito1.example.com.timeperception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

public class ActiveSendFile extends Service {
    //tag para usar en los logs
    final String TAG = "SendFile";


    //funcion para enviar los archivos de textos al servidor
    public void SendTheLogs() {
        //variables de cambiar el tiempo de envio
        long minuto = 1000 * 60;
        long hora = minuto * 60;
        long dia = hora * 24;

        //log de para saber si se entra a esta funcion
        Log.d(TAG,"Entre a la alarma ActiveSendFile");
        Calendar SendfileStart = Calendar.getInstance();

        //set the time to midnight tonight

        SendfileStart.set(Calendar.HOUR_OF_DAY, 0);
        SendfileStart.set(Calendar.MINUTE,0);
        SendfileStart.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        //create a pending intent to be called at midnight
        Intent intent = new Intent(getApplicationContext(),SendFile.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        Log.d(TAG,"crear el repetidor");

        am.setRepeating(AlarmManager.RTC_WAKEUP, SendfileStart.getTimeInMillis(), hora, midnightPI);

    }

    @Override
    public void onCreate() {
//        Log.d(TAG,"Entre a oncreate");
//        SendTheLogs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Entre a oncreate");
        SendTheLogs();
        return super.onStartCommand(intent, flags, startId);

    }


}
