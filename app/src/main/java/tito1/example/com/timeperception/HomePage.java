package tito1.example.com.timeperception;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;

import javax.crypto.Cipher;


public class HomePage extends AppCompatActivity {


    @SuppressLint({"WrongConstant", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        Log.d("valor", String.valueOf(firtlog.getBoolean("llenoCuestionario?", false)));
        if (firtlog.getBoolean("llenoCuestionario?", false) == true){
            setContentView(R.layout.activity_home_page);
            Object question07 = R.id.question07;
            TextView UserID = findViewById(R.id.userID);
            UserID.setText("UserID: " + questionnaireAnswers.getString(question07.toString(),""));
            if(isMyServiceRunning(Services.class)){
                UserID.setText("SI esta funcionando");
            }else{
                startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), 0);
            }


        }
        else{
            //enviar a la pagina del cuestionario
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            startActivity(intent1);
            }

    }

//    public void geo(View view){
//        Intent intent = new Intent(getApplicationContext(),Geolocalization.class);
//        startActivity(intent);
//    }

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
         else if (item.getItemId() == R.id.questionary) {
            Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
            startActivity(intent);
            return true;
        }
        return false;
    }





}