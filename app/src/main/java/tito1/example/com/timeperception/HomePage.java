package tito1.example.com.timeperception;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class HomePage extends AppCompatActivity {


    final String TAG = "TP-Smart";
//    private Button boton;
//    private NotificationManagerCompat notificacionManager;



    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences idioma = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        //configuracion del idioma
        Log.d(TAG,"Este es el idioma" +idioma.getString("idoma",""));
        Configuration conf = getResources().getConfiguration();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        if(idioma.getString("idioma", "").equals("")){
            conf.locale = new Locale("es");
            res.updateConfiguration(conf, dm);
        }else{
            conf.locale = new Locale(idioma.getString("idioma",""));
            res.updateConfiguration(conf, dm);
        }






        //variable que guarda si ya lleno el cuestionario o no.
        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        Log.d(TAG, "HomePage Se lleno el questionario: "+String.valueOf(firtlog.getBoolean("llenoCuestionario?", false)));
        if (firtlog.getBoolean("llenoCuestionario?", false) == true){
            //Nose se hace nada Porque fun onResume se encarga.
//            SetNotificationFinal.setAlarmFinalQuestionnaire(this);
//            SetNotification.setAlarmPerseption(this);

        }
        else{
            //enviar a la pagina del cuestionario
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            startActivity(intent1);


//          configuracion del las notificaciones
//            Log.d(TAG,"PerseptionQuestion Llamando la noficacion");
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY,12);
//            calendar.set(Calendar.MINUTE,30);
//            calendar.set(Calendar.SECOND,0);
//            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//            //create a pending intent to be called at midnight
////            Intent intent = new Intent(getApplicationContext(),SetNotification.class);
////            PendingIntent midnightPI =  PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
////            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60, midnightPI);
//            SetNotificationFinal.setAlarmFinalQuestionnaire(this);
//            SetNotification.setAlarmPerseption(this);




        }
    }

    @Override
    protected void onResume() {
        setContentView(R.layout.activity_home_page);
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        //saber si el servicio esta corriendo al volver entrar en la app
        Button active = findViewById(R.id.active);
        TextView goActiveService = findViewById(R.id.goActive);
        String mensaje = "";
        Object question07 = R.id.question07;
        TextView UserID = findViewById(R.id.ID);

        //llenar cada mensaje con su respuesta
        mensaje = mensaje + getString(R.string.userID)+" "+
                questionnaireAnswers.getString(question07.toString(),"no hay");
        mensaje = mensaje + "\n"+getString(R.string.ultimoEnvio)+" "
                + questionnaireAnswers.getString("last","no hay");
        UserID.setText(mensaje);

        //Si el sevicio esta corriendo no aparecera nada, si no esta aparecera una opcion para activarlo
        if(isMyServiceRunning(Services.class)){
            active.setVisibility(View.GONE);
            goActiveService.setVisibility(View.GONE);

        }else{
            active.setVisibility(VISIBLE);
            goActiveService.setText(getString(R.string.activarServicio));
            active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            , 0);
                }
            });
        }
        super.onResume();
    }

    //metodo que verifica si algun servicio esta encendido
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //Menu de la app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    //opciones del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.language) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);

            return true;
        }
        /*ELIMINAR ESTA OPCION PARA LA PRUEBA*/
         else if (item.getItemId() == R.id.questionary) {
            Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.questionaryfinal) {
            Intent intent = new Intent(getApplicationContext(), FinalQuestionnaire.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

}