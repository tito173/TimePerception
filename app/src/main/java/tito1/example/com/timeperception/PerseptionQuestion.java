package tito1.example.com.timeperception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

public class PerseptionQuestion extends AppCompatActivity {

    final String TAG = "TP-Smart";
//    private NotificationManagerCompat notificacionManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perseption_question);

//        notificacionManager = NotificationManagerCompat.from(this);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        int hora = 1000 * 60 * 60;

        Log.d(TAG,"PerseptionQuestion Llamando la noficacion");



        //create a pending intent to be called at midnight
        Intent intent = new Intent(getApplicationContext(),SetNotification.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), hora, midnightPI);


    }

    //Si el usuario no ha seleccionado respuesta mantener lo en la misma activdad sin poder ver el video.
    @Override
    protected void onRestart() {
        setContentView(R.layout.activity_perseption_question);
        final TextView instruction =  findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final VideoView videoView = findViewById(R.id.videoView2);

        videoView.          setVisibility(View.INVISIBLE);
        startPerseption.    setVisibility(View.INVISIBLE);
        instruction.setVisibility(View.VISIBLE);
        instruction.setTextSize(20);
        instruction.setText("Cuantos segundos duro el video?");

        super.onRestart();
    }

    @Override
    protected void onResume() {
        setContentView(R.layout.activity_perseption_question);

        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        final TextView instruction =  findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final Button save = findViewById(R.id.save);
        final VideoView videoView = findViewById(R.id.videoView2);
        final RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);

        videoView.          setVisibility(View.INVISIBLE);
        radioButtonGroup.   setVisibility(View.INVISIBLE);
        save.               setVisibility(View.INVISIBLE);
        instruction.setTextSize(20);
        instruction.setText("Press the button to start the test");

        startPerseption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPerseption.    setVisibility(View.INVISIBLE);
                instruction.        setVisibility(View.INVISIBLE);
                videoView.          setVisibility(View.VISIBLE);
                save.               setVisibility(View.VISIBLE);
                radioButtonGroup.   setVisibility(View.VISIBLE);
                String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Execute code here

                        videoView.stopPlayback();
                        videoView.  setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.VISIBLE);
                        instruction.setText("Cuantos segundos duro el video?");
                    }
                }, 3000);
            }


        });

        super.onResume();
    }

    public void Save(View view){

        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonId);
        int indice = radioButtonGroup.indexOfChild(radioButton);

        //Si no se selecciono una respuesta inicia la actividad, "Es provivionar a lo que se resuelve el error
        //producidor por continuar sin seleccionar alguna opcion"
        if(indice == -1){
//            Intent intent = new Intent(getApplicationContext(),PerseptionQuestion.class);
//            startActivity(intent);
            onRestart();
            return;
        }
        try {
                questionnaireAnswers.edit().putInt("questionvideo", indice).apply();

            } catch (Exception e) {
                Log.d(TAG, "PerseptionQuestion Error al saber la respues");
            }

        //close TP-Smart
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        startActivity(intent);
//        sendOnChannel1(view);
//        finish();
//        moveTaskToBack(true);
        }


}
