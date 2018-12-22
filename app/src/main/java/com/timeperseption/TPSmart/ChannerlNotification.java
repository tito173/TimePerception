package com.timeperseption.TPSmart;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class ChannerlNotification extends Application {
    //crea los canales para que las notificaciones se muestren en versiones >= 8.0.0

    public static final String CHANNEL_1_ID = "Channel1";
    public static final String CHANNEL_2_ID = "Channel2";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();

        createNotifactionChannel1();
        createNotifactionChannel2();

    }

    //canal 1
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createNotifactionChannel1() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Perseption Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }

    //canal 2
    private void createNotifactionChannel2() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Final Questionnaire",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2");
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel2);
        }
    }
}
