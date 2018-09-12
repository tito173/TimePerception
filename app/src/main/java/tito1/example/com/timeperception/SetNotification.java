package tito1.example.com.timeperception;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class SetNotification extends BroadcastReceiver{

    final String TAG = "TP-Smart";
    private NotificationManagerCompat notificacionManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"SetNotification fun onReceive la noficacion");
        notificacionManager = NotificationManagerCompat.from(context);

        sendOnChannel1(context);
    }
    public void sendOnChannel1(Context context1){

        Intent intent = new Intent(context1, PerseptionQuestion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(context1,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Titulo")
                .setContentText("Mensaje")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificacionManager.notify(1,notifiaction);

    }

}
