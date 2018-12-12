package com.timeperseption.TPSmart;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import com.timeperception.TPSmart.R;

import static android.view.View.VISIBLE;


public class HomePage extends AppCompatActivity {


    final String TAG = "TP-Smart";
    private MyServiceIsRunning myServiceIsRunning;


    public  static TextView data;

    private LocationManager locationManager;
    ;
    private LocationListener locationListener, locationListenerNetwork;

    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences idioma = this.getSharedPreferences("com.timeperseption.TPSmart", Context.MODE_PRIVATE);

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
        SharedPreferences firtlog = this.getSharedPreferences("com.timeperseption.TPSmart", Context.MODE_PRIVATE);

        Log.d(TAG, "HomePage Se lleno el questionario: "+String.valueOf(firtlog.getBoolean("llenoCuestionario?", false)));
        if (firtlog.getBoolean("llenoCuestionario?", false)){
            Intent intentService = new Intent(getApplicationContext(),MyServiceIsRunning.class);
            startService(intentService);
//            try {
//              SetNotification.setAlarmPerseption(this);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
////            //para hacer pruebas una vez el cuestionario esta lleno y no volver a llenar otro.
//            SendTheLogs(getApplicationContext());

        }
        else{
            //enviar a la pagina del cuestionario
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);

            SetNotification.setAlarmPerseption(this);
            Log.d("FetchData Notification","Notification home active");

        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED){
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime, 0, locationListenerNetwork);
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);
//
//            }
//        }
//    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        setContentView(R.layout.activity_home_page);

//para desinstalar la app
//        Button b = findViewById(R.id.unistall);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent5 = new Intent(getApplicationContext(),UnistallApp.class);
//                startActivity(intent5);
//            }
//        });

        //geolocalizacion
        /********************************************************************************************/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            Log.d("Location", "IF verification permition");

        }

        /*****************************************************************************************/

        SharedPreferences questionnaireAnswers = this.getSharedPreferences("com.timeperseption.TPSmart",Context.MODE_PRIVATE);

        //saber si el servicio esta corriendo al volver entrar en la app
        Button active = findViewById(R.id.active);
        TextView goActiveService = findViewById(R.id.goActive);
        Button activeGps = findViewById(R.id.activeGps);
        TextView goActiveServiceGps = findViewById(R.id.goActiveGps);
        String mensaje = "";
        Object question07 = R.id.question07;
        TextView UserID = findViewById(R.id.ID);

        //llenar cada mensaje con su respuesta
        mensaje = mensaje + getString(R.string.userID)+" "+
                questionnaireAnswers.getString(question07.toString(),"no hay");
        mensaje = mensaje + "\n"+getString(R.string.ultimoEnvio)+" "
                + questionnaireAnswers.getString("last","no hay");
        mensaje = mensaje + "\n"+getString(R.string.tecnicalSupport);
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
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( locationManager!= null ){

            if(locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ) {
                // Call your Alert message
//                Log.d("gpss","SI");
                activeGps.setVisibility(View.GONE);
                goActiveServiceGps.setVisibility(View.GONE);

            } else {
//                Log.d("gpss","No");
                activeGps.setVisibility(VISIBLE);
                goActiveServiceGps.setText(getString(R.string.activarServicioGps));
                activeGps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                , 0);
                    }
                });
            }

        }else {                Log.d("gpss","Es null");
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
    private boolean isMyServiceRunning(String serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.equals(service.service.getClassName())) {
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
        }
//        else if (item.getItemId() == R.id.unistall) {
//            Intent intent5 = new Intent(getApplicationContext(),UnistallApp.class);
//            startActivity(intent5);
//            return true;
//        }
        return false;
    }

}