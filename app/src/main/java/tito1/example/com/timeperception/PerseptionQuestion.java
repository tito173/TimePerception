package tito1.example.com.timeperception;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;

import java.util.ArrayList;

public class PerseptionQuestion extends AppCompatActivity {

    ArrayList<String> numberQuestion = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perseption_question);

        final VideoView videoView = findViewById(R.id.videoView2);
        String path = ("android.resource://" + getPackageName() + "/" + R.raw.demo);
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Execute code here

                videoView.stopPlayback();
                videoView.setVisibility(View.INVISIBLE);
            }
        }, 3000);


        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        numberQuestion.add("questionvideo");

//        ArrayList questions = new ArrayList();
//        questions.add(R.id.radioGroupvideo);
        RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        int value = questionnaireAnswers.getInt("questionvideo",0);
        Log.d("valor del radiogroup", Integer.toString(value));
        if (value != -1){
            RadioButton rb = (RadioButton) radioButtonGroup.getChildAt(value);
            rb.setChecked(true);
        }






    }
    public  void Save(View view){


        ArrayList questions = new ArrayList();
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        questions.add(R.id.radioGroupvideo);


        RadioGroup radioButtonGroup = findViewById(R.id.radioGroupvideo);
        int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonId);
        int indice = radioButtonGroup.indexOfChild(radioButton);
        try {
                questionnaireAnswers.edit().putInt("questionvideo", indice).commit();

            } catch (Exception e) {
                Log.d("ERROR Video", "Error");
            }

        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        }




}
