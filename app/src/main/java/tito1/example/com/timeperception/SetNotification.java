package tito1.example.com.timeperception;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;

public class SetNotification extends BroadcastReceiver{

    final String TAG = "TP-Smart";
    private NotificationManagerCompat notificacionManager;
    String estado = "";
    String message ="";


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SetNotificaion","SetNotification fun onReceive la noficacion");
        notificacionManager = NotificationManagerCompat.from(context);
        SharedPreferences day = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

            try {
                //verificar el stado de las notificaciones
                notifycationDay(context);

                //json con el contenido de la proxima accion a tomar
                JSONObject jsonObject = new JSONObject(FetchData.data);

                //guardar el dia que corresponde a la prueba a tomar
                day.edit().putString("day_notification",jsonObject.getString("day")).apply();

                //Parsear el contenido del estado y demas componentes del json
                estado = jsonObject.getString("status");
                message += "Presione para acceder a la prueba del día " +jsonObject.getString("day");
                int dia = Integer.parseInt(jsonObject.getString("day"));

                //si el estado es alert muestra la noficacion pertinente
                if(estado.equals("alert") && dia < 8){
                    sendOnChannel1(context);
                    Log.d("SetNotificaion","Se mostro una notificacion");
                }else if (estado.equals("alert") && dia >= 8){
                    sendOnChannel2(context);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }




    public  void sendOnChannel1(Context context1) {

        Log.d("SetNotificaion","sendOnChannela");

        Intent intent = new Intent(context1, PerseptionQuestion.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,intent,0);


        Notification notifiaction = new NotificationCompat.Builder(context1, ChannerlNotification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Prueba de Percepción de tiempo")
                .setContentText(message)
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
    public  void sendOnChannel2(Context context1){

        Intent intent = new Intent(context1, FinalQuestionnaire.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(context1, ChannerlNotification.CHANNEL_2_ID)
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
    public static void setAlarmPerseption(Context context) {

        Log.d("SetNotificaion","configuracion de notificacion");
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context,SetNotification.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        assert am != null;
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),AlarmManager.INTERVAL_HOUR, midnightPI);

    }
    public static void notifycationDay(Context context) throws InterruptedException, ExecutionException {
        SharedPreferences user_id = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        Object question07 = R.id.question07;
        FetchData process = new FetchData(user_id.getString(question07.toString(),""),true,"notificacion");
        process.execute().get();

    }



}
