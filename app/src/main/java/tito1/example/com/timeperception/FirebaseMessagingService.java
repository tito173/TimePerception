package tito1.example.com.timeperception;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 2/20/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService  {
    private static final String TAG = "FirebaseMessagingServic1";
    private NotificationManagerCompat notificacionManager;

    public FirebaseMessagingService() {

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, " DATA: "+remoteMessage.getData());
        JSONObject object = new JSONObject(remoteMessage.getData());


        try {
            if (object.get("valor2") != null) {
                Log.d(TAG, object.toString());
                sendNotificaiton2();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (object.get("valor1")!=null) {
                Log.d(TAG, object.toString());

                sendNotification1();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification1() {
        notificacionManager = NotificationManagerCompat.from(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), PerseptionQuestion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(getApplicationContext(),App.CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
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
    private void sendNotificaiton2(){
        notificacionManager = NotificationManagerCompat.from(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), FinalQuestionnaire.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(getApplicationContext(),App.CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
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
}

