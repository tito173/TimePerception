package tito1.example.com.timeperception;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Arrays.asList;

public class PerseptionQuestion extends AppCompatActivity {

    final String TAG = "TP-Smart";
    int duracion1;
    int duracion2;
    int duracion3;
    int duracion4;


//    private NotificationManagerCompat notificacionManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perseption_question);

    }

//    //Si el usuario no ha seleccionado respuesta mantener lo en la misma activdad sin poder ver el video.
//    @Override
//    protected void onRestart() {
//        setContentView(R.layout.activity_perseption_question);
//        final TextView instruction =  findViewById(R.id.instruction);
//        final Button startPerseption = findViewById(R.id.startPerseption);
//        final ImageView cuadrado   = findViewById(R.id.imagen);
//
//
//        startPerseption.    setVisibility(View.INVISIBLE);
//        instruction.setVisibility(View.VISIBLE);
//        instruction.setTextSize(20);
//        instruction.setText("Cuantos segundos duro el video?");
//
//        super.onRestart();
//    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {

        super.onResume();
        setContentView(R.layout.activity_perseption_question);

        //configurar un orden aleatoreo para colocar las imagenes
        Set<Integer> set = new LinkedHashSet<>();
        set.clear();
        while (set.size() < 4) {
            set.add((int) (Math.random() * (4)));
        }



        //convetir el conjuto en un arreglo
        final Integer[] arr = set.toArray(new Integer[set.size()]);

        //crear un arreglo de lista para almacenar el llamado de las figuras
        final ArrayList lista = new ArrayList();
        lista.add(R.drawable.cuadrado);
        lista.add(R.drawable.rectangulo);
        lista.add(R.drawable.triangulo);
        lista.add(R.drawable.estrella);

        prueba1(arr,lista);

    }

    private void prueba1(final Integer[] arr, final ArrayList lista) {

        final TextView instruction = findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final Button save = findViewById(R.id.save);
        final RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        final ImageView imageView = findViewById(R.id.imagen);

        //configurar las opciones de duracion aleatorea de las imagenes
        final ArrayList<Long> durationOp = new ArrayList<Long>();
        durationOp.add((long) 400);
        durationOp.add((long) 500);
        durationOp.add((long) 1000);
        durationOp.add((long) 2000);
        durationOp.add((long) 5000);
        durationOp.add((long) 6000);
        duracion1 = (int) (Math.random() * (6));

        //montar la imagen
        imageView.setImageResource((Integer) lista.get(arr[0]));
        imageView.setVisibility(View.INVISIBLE);
        radioButtonGroup.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        instruction.setTextSize(20);
        instruction.setText(getString(R.string.instriction));

        startPerseption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                startPerseption.setVisibility(View.INVISIBLE);
                instruction.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                radioButtonGroup.setVisibility(View.VISIBLE);
                String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Execute code here
                        imageView.setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        instruction.setText("Cuantos segundos duro el video?");
                    }
                }, durationOp.get(duracion1));
            }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences questionnaireAnswers = getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
                RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);

                //Si no se selecciono una respuesta inicia la actividad, "Es provivionar a lo que se resuelve el error
                //producidor por continuar sin seleccionar alguna opcion"
                if (indice == -1) {
                    onRestart();
                    return;
                }
                prueba2(arr,lista);

                try {
                    questionnaireAnswers.edit().putInt("TestResp1", indice+1).apply();

                } catch (Exception e) {
                    Log.d(TAG, "PerseptionQuestion Error al saber la respues");
                }

            }

        });
    }
    private void prueba2(final Integer[] arr, final ArrayList lista) {




        final TextView instruction = findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final Button save = findViewById(R.id.save);
        final RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        final ImageView imageView = findViewById(R.id.imagen);
        //configurar las opciones de duracion aleatorea de las imagenes
        final ArrayList<Long> durationOp = new ArrayList<Long>();
        durationOp.add((long) 400);
        durationOp.add((long) 500);
        durationOp.add((long) 1000);
        durationOp.add((long) 2000);
        durationOp.add((long) 5000);
        durationOp.add((long) 6000);
        duracion2 = (int) (Math.random() * (6));

        imageView.setImageResource((Integer) lista.get(arr[1]));
        startPerseption.setVisibility(View.VISIBLE);
        instruction.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        radioButtonGroup.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        instruction.setTextSize(20);
        instruction.setText(getString(R.string.instriction));

        startPerseption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                startPerseption.setVisibility(View.INVISIBLE);
                instruction.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                radioButtonGroup.setVisibility(View.VISIBLE);
                String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Execute code here
                        imageView.setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        instruction.setText("Cuantos segundos duro el video?");
                    }
                }, durationOp.get(duracion2));
            }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences questionnaireAnswers = getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
                RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);

                //Si no se selecciono una respuesta inicia la actividad, "Es provivionar a lo que se resuelve el error
                //producidor por continuar sin seleccionar alguna opcion"
                if (indice == -1) {
                    onRestart();
                    return;
                }
                prueba3(arr,lista);

                try {
                    questionnaireAnswers.edit().putInt("TestResp2", indice+1).apply();

                } catch (Exception e) {
                    Log.d(TAG, "PerseptionQuestion Error al saber la respues");
                }

            }

        });
    }
    private void prueba3(final Integer[] arr, final ArrayList lista) {



        final TextView instruction = findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final Button save = findViewById(R.id.save);
        final RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        final ImageView imageView = findViewById(R.id.imagen);
        //configurar las opciones de duracion aleatorea de las imagenes
        final ArrayList<Long> durationOp = new ArrayList<Long>();
        durationOp.add((long) 400);
        durationOp.add((long) 500);
        durationOp.add((long) 1000);
        durationOp.add((long) 2000);
        durationOp.add((long) 5000);
        durationOp.add((long) 6000);
        duracion3 = (int) (Math.random() * (6));

        imageView.setImageResource((Integer) lista.get(arr[2]));
        startPerseption.setVisibility(View.VISIBLE);
        instruction.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        radioButtonGroup.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        instruction.setTextSize(20);
        instruction.setText(getString(R.string.instriction));

        startPerseption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                startPerseption.setVisibility(View.INVISIBLE);
                instruction.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                radioButtonGroup.setVisibility(View.VISIBLE);
                String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Execute code here
                        imageView.setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        instruction.setText("Cuantos segundos duro el video?");
                    }
                }, durationOp.get(duracion3));
            }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences questionnaireAnswers = getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
                RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);

                //Si no se selecciono una respuesta inicia la actividad, "Es provivionar a lo que se resuelve el error
                //producidor por continuar sin seleccionar alguna opcion"
                if (indice == -1) {
                    onRestart();
                    return;
                }
                prueba4(arr,lista);

                try {
                    questionnaireAnswers.edit().putInt("TestResp3", indice+1).apply();

                } catch (Exception e) {
                    Log.d(TAG, "PerseptionQuestion Error al saber la respues");
                }

            }

        });
    }
    private void prueba4(final Integer[] arr, final ArrayList lista) {

        final TextView instruction = findViewById(R.id.instruction);
        final Button startPerseption = findViewById(R.id.startPerseption);
        final Button save = findViewById(R.id.save);
        final RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        final ImageView imageView = findViewById(R.id.imagen);
        //configurar las opciones de duracion aleatorea de las imagenes
        final ArrayList<Long> durationOp = new ArrayList<Long>();
        durationOp.add((long) 400);
        durationOp.add((long) 500);
        durationOp.add((long) 1000);
        durationOp.add((long) 2000);
        durationOp.add((long) 5000);
        durationOp.add((long) 6000);
        duracion4 = (int) (Math.random() * (6));

        imageView.setImageResource((Integer) lista.get(arr[3]));
        startPerseption.setVisibility(View.VISIBLE);
        instruction.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        radioButtonGroup.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        instruction.setTextSize(20);
        instruction.setText(getString(R.string.instriction));

        startPerseption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.VISIBLE);
                startPerseption.setVisibility(View.INVISIBLE);
                instruction.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                radioButtonGroup.setVisibility(View.VISIBLE);
                String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Execute code here
                        imageView.setVisibility(View.INVISIBLE);
                        instruction.setVisibility(View.INVISIBLE);
                        instruction.setText("Cuantos segundos duro el video?");
                    }
                }, durationOp.get(duracion4));
            }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences questionnaireAnswers = getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
                RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);

                //Si no se selecciono una respuesta inicia la actividad, "Es provivionar a lo que se resuelve el error
                //producidor por continuar sin seleccionar alguna opcion"
                if (indice == -1) {
                    onRestart();
                    return;
                }


                try {
                    questionnaireAnswers.edit().putInt("TestResp4", indice+1).apply();

                } catch (Exception e) {
                    Log.d(TAG, "PerseptionQuestion Error al saber la respues");
                }
                try {
                    SendFile.SendResPerseptionTest(getApplicationContext(),
                            durationOp.get(duracion1),durationOp.get(duracion2),durationOp.get(duracion3),durationOp.get(duracion4));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(),HomePage.class);
                startActivity(intent);

            }

        });

    }


}





