package tito1.example.com.timeperception;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import javax.crypto.Cipher;

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


        // varaibale para almacenar el ID del usuario.
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        //Llave para la encriotacion del file
        String key = "This is a secret";
        Crypto crypto = new Crypto();

        final File testFile = new File(getApplicationContext().getExternalFilesDir(null), "TestFile.txt");
        final File testFile2 = new File(getApplicationContext().getExternalFilesDir(null), "TestFile2.txt");
        final File testFile3 = new File(getApplicationContext().getExternalFilesDir(null), "TestFileDes.txt");

        Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,testFile2,testFile3);

        //variable que guarda si ya lleno el cuestionario o no.
        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        Log.d(TAG, "HomePage Se lleno el questionario: "+String.valueOf(firtlog.getBoolean("llenoCuestionario?", false)));
        if (firtlog.getBoolean("llenoCuestionario?", false) == true){
            //Nose se hace nada Porque fun onResume se encarga.
        }
        else{
            //enviar a la pagina del cuestionario
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            startActivity(intent1);
            }
    }

    @Override
    protected void onResume() {
        setContentView(R.layout.activity_home_page);
        /*/////////////////////////////////////////////////////////////

        boton = (Button) findViewById(R.id.boton);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOnChannel1(view);

            }
        });




        /////////////////////////////////////////////////////////*/

        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        //saber si el servicio esta corriendo al volver entrar en la app
        Button active = findViewById(R.id.active);
        TextView goActiveService = findViewById(R.id.goActive);
        String mensaje = "";
        Object question07 = R.id.question07;
        TextView UserID = findViewById(R.id.ID);

        //llenar cada mensaje con su respuesta
        mensaje = mensaje + "User ID: "+questionnaireAnswers.getString(question07.toString(),"no hay");
        mensaje = mensaje + "\nLast send was: " + questionnaireAnswers.getString("last","no hay");
        UserID.setTextSize(15);
        UserID.setTextColor(Color.parseColor("#000000"));
        UserID.setText(mensaje);

        //Si el sevicio esta corriendo no aparecera nada, si no esta aparecera una opcion para activarlo
        if(isMyServiceRunning(Services.class)){
            active.setVisibility(INVISIBLE);
            goActiveService.setVisibility(INVISIBLE);

        }else{
            active.setVisibility(VISIBLE);
            goActiveService.setText("Press that button \"Active Service\" and active TP-Smart service to start the app");
            goActiveService.setTextSize(15);
            goActiveService.setTextColor(Color.parseColor("#000000"));
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

    /*//////////////////////////////////////////////////////
    public void sendOnChannel1(View view){

        Intent intent = new Intent(this, PerseptionQuestion.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        Notification notifiaction = new NotificationCompat.Builder(this,App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Titulo")
                .setContentText("Mensaje")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificacionManager.notify(1,notifiaction);

    }
    ///////////////////////////////////////////////////////////*/


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
        }
        return false;
    }

}