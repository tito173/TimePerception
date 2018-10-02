package tito1.example.com.timeperception;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.getIntent;

public class SetNotification extends BroadcastReceiver{

    final String TAG = "TP-Smart";
    private NotificationManagerCompat notificacionManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("case","SetNotification fun onReceive la noficacion");
        notificacionManager = NotificationManagerCompat.from(context);
            sendOnChannel1(context);
            Log.d("cases", "este es el 0");

    }

    public  void sendOnChannel1(Context context1){

        Intent intent = new Intent(context1, PerseptionQuestion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(context1,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Test Perception")
                .setContentText("Complete the next test.")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
        notificacionManager.notify(1,notifiaction);

    }
    public static void setAlarmPerseption(Context context) {

        Log.d("case","configuracion de notificacion");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,30);
        calendar.set(Calendar.SECOND,0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context,SetNotification.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        assert am != null;
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, midnightPI);

    }



}
