package tito1.example.com.timeperception;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


public class SetNotificationFinal extends BroadcastReceiver {
    final String TAG = "TP-Smart";
    private NotificationManagerCompat notificacionManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        notificacionManager = NotificationManagerCompat.from(context);
        sendOnChannel2(context);
        Log.d("cases", "este es el 1");
    }
    public  void sendOnChannel2(Context context1){

        Intent intent = new Intent(context1, FinalQuestionnaire.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(context1,App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Final Questionnaire")
                .setContentText("Pleas complete the following questionnaire.")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
        notificacionManager.notify(2,notifiaction);

    }

    public static void setAlarmFinalQuestionnaire(Context context) {

        Log.d("case","configuracion de notificacion final");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.YEAR,2018);
        calendar.set(Calendar.DAY_OF_MONTH,27);
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,30);
        Log.d("TIEMPO",calendar.getTime().toString());

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context,SetNotificationFinal.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(context,0,intent,0);
        assert am != null;
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), midnightPI);

    }
}
