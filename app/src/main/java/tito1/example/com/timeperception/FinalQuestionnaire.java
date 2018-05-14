package tito1.example.com.timeperception;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;
/* librerias para pobrar si funcionan

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
*/
import java.util.ArrayList;

public class FinalQuestionnaire extends AppCompatActivity {

    private final String TAG = "test";
    //    private static final String TAG = "test";
    ArrayList<String> numberQuestion = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_questionnaire);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        VideoView videoView = (VideoView) findViewById(R.id.video);
        videoView.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.video);
        videoView.start();

        //        String videoPath = "android.resource://"+getPackageName()+"/"+R.raw.video;
//        Uri uri =Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//
//        MediaController mediaController = new MediaController(this);
//        videoView.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);


        numberQuestion.add("question09");
        numberQuestion.add("question16");

        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup09);
        questions.add(R.id.radioGroup16);

        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

        for (int i = 0 ; i < questions.size(); i++) {
            RadioGroup radioButtonGroup = (RadioGroup) findViewById((Integer) questions.get(i));
            int value = questionnaireAnswers.getInt(numberQuestion.get(i), -1);
            RadioButton rb = (RadioButton) radioButtonGroup.getChildAt(value);
            try {
                ((RadioButton) radioButtonGroup.getChildAt(value)).setChecked(true);
            } catch (Exception e) {
                Log.d(TAG, "Error when open" + i);
            }

        }

        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        for (int i = 0 ; i < question07.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question07[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question07[i].toString(),false));
        }
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_8,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        for (int i = 0 ; i < question08.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question08[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question08[i].toString(),false));
        }
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
        for (int i = 0 ; i < question10.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question10[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question10[i].toString(),false));
        }
        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};
        for (int i = 0 ; i < question11.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question11[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question11[i].toString(),false));
        }








        Object[] question02 = {R.id.question12,R.id.question13,R.id.question14,R.id.question15};
        for (int i = 0 ; i< question02.length; i++){

            EditText valueOfQuestion = findViewById((Integer) question02[i]);
            valueOfQuestion.setText(questionnaireAnswers.getString(question02[i].toString(),"777"));
        }



    }

    public void saveQuestionnaire(View view){
        ArrayList questions = new ArrayList();
        ArrayList answerQuestions = new ArrayList();


        //to erase the data save
//        questions.clear();
        //save every option from radiogroup
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        questions.add(R.id.radioGroup09);questions.add(R.id.radioGroup16);
        for (int i = 0 ; i < questions.size(); i++){
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
            View radioButton = radioButtonGroup.findViewById(radioButtonId);
            int indice = radioButtonGroup.indexOfChild(radioButton);
            try {
                questionnaireAnswers.edit().putInt(numberQuestion.get(i),indice).commit();
            }catch (Exception  e){
                Log.d(TAG,"Error");
            }
        }

        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_5,R.id.question8_6,R.id.question8_7,R.id.question8_8};
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};

        for (int i = 0 ; i < question07.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question07[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question07[i].toString(),true).commit();
            }
            else {
                questionnaireAnswers.edit().putBoolean(question07[i].toString(),false).commit();
            }
        }

        for (int i = 0 ; i < question08.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question08[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question08[i].toString(),true).commit();
            }
            else {
                questionnaireAnswers.edit().putBoolean(question08[i].toString(),false).commit();
            }
        }
        for (int i = 0 ; i < question10.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question10[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question10[i].toString(),true).commit();
            }
            else {
                questionnaireAnswers.edit().putBoolean(question10[i].toString(),false).commit();
            }
        }
        for (int i = 0 ; i < question11.length;i++){
            CheckBox box = (CheckBox) findViewById((Integer) question11[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question11[i].toString(),true).commit();
            }
            else {
                questionnaireAnswers.edit().putBoolean(question11[i].toString(),false).commit();
            }
        }

        Object[] question02 = {R.id.question12,R.id.question13,R.id.question14,R.id.question15};

        for (int i = 0 ; i< question02.length; i++) {
            EditText valueOfQuestion = findViewById((Integer) question02[i]);
            if (valueOfQuestion == null ){
                questionnaireAnswers.edit().putString(question02[i].toString(),"").commit();

            }else {
                questionnaireAnswers.edit().putString(question02[i].toString(), valueOfQuestion.getText().toString()).commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.language){
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);

            return  true;
        }
        return  false;
    }
}

