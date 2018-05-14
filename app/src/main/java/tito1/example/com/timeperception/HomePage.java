package tito1.example.com.timeperception;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;


public class HomePage extends AppCompatActivity {

    private String url = "http://104.131.32.115/save_file.php";



    public void questionarieResult(View view){
        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        Map<String,?> keys = firtlog.getAll();
        Log.d("map values","entre");


        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

//        firtlog.edit().clear().apply();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
//                return;
//            }
//        }


        if (firtlog.getBoolean("firtlog", false) == true){
            setContentView(R.layout.activity_home_page);
            testReapeat();
        }
        else{
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            startActivity(intent1);
            firtlog.edit().putBoolean("firstLog",true).apply();

            try {
                // Creates a file in the primary external storage space of the
                // current application.
                // If the file does not exists, it is created.

                File testFile = new File(this.getExternalFilesDir(null), "FileInstallation.txt");
                if (!testFile.exists())
                    testFile.createNewFile();

                // Adds a line to the file
//          to eraise the content of file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false ));
//            writer.write("");


//                BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
                writer.write("The app was installed");

                Map<String, ?> allEntries = firtlog.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                }
                writer.newLine();
//
                writer.close();
                // Refresh the data so it can seen when the device is plugged in a
                // computer. You may have to unplug and replug the device to see the
                // latest changes. This is not necessary if the user should not modify
                // the files.
                MediaScannerConnection.scanFile(this,
                        new String[]{testFile.toString()},
                        null,
                        null);
            } catch (IOException e) {
                Log.e("Test", "Unable to write to the TestFile.txt file.");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.language) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.questionary) {
            Intent intent = new Intent(getApplicationContext(), Questionnaire.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


    public void testReapeat() {
        long minuto = 1000 * 60;
        long hora = minuto * 60;
        long dia = hora * 24;
        //create new calendar instance
        Log.d("Test","prepare el pendingintent");
        Calendar SendfileStart = Calendar.getInstance();

        //set the time to midnight tonight

        SendfileStart.set(Calendar.HOUR_OF_DAY, 12);
        SendfileStart.set(Calendar.MINUTE,0);
        SendfileStart.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) HomePage.this.getSystemService(ALARM_SERVICE);

        //create a pending intent to be called at midnight
        Intent intent = new Intent(getApplicationContext(),SendFile.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);


        am.setRepeating(AlarmManager.RTC_WAKEUP, SendfileStart.getTimeInMillis(), minuto*2, midnightPI);

    }

}