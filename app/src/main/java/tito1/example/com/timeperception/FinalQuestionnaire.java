package tito1.example.com.timeperception;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


public class FinalQuestionnaire extends AppCompatActivity {


    private final String TAG = "TP-Smart";
    ArrayList<String> numberQuestion = new ArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_final_questionnaire);
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        SharedPreferences idioma = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

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

        TextView error  = findViewById(R.id.error1);
        String fillError = questionnaireAnswers.getString("error","");
        if(fillError.equals(""))
            error.setVisibility(View.GONE);
        error.setText(fillError);
        error.setTextSize(20);
        error.setTextColor(Color.parseColor("#FF0040"));
        questionnaireAnswers.edit().remove("error").apply();

        numberQuestion.add("question09");
        numberQuestion.add("question16");

        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup09);
        questions.add(R.id.radioGroup16);

        for (int i = 0 ; i < questions.size(); i++) {
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            int value = questionnaireAnswers.getInt(numberQuestion.get(i), -1);
            RadioButton rb = (RadioButton) radioButtonGroup.getChildAt(value);
            try {
                ((RadioButton) radioButtonGroup.getChildAt(value)).setChecked(true);
            } catch (Exception e) {
                Log.d(TAG, "FinalQuestionanire Error when open question " + i);
            }

        }

        //Marcar todos los check boxes que allan
        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        for (int i = 0 ; i < question07.length;i++){
            CheckBox box = findViewById((Integer) question07[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question07[i].toString(),false));
        }

        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_4,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        for (int i = 0 ; i < question08.length;i++){
            CheckBox box = findViewById((Integer) question08[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question08[i].toString(),false));
        }

        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
        for (int i = 0 ; i < question10.length;i++){
            CheckBox box = findViewById((Integer) question10[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question10[i].toString(),false));
        }

        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};
        for (int i = 0 ; i < question11.length;i++){
            CheckBox box = findViewById((Integer) question11[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question11[i].toString(),false));
        }

        Object[] question02 = {R.id.question12,R.id.question13,R.id.question14,R.id.question15};
        for (int i = 0 ; i< question02.length; i++){

            EditText valueOfQuestion = findViewById((Integer) question02[i]);
            valueOfQuestion.setText(questionnaireAnswers.getString(question02[i].toString(),""));
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_questionnaire);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void saveQuestionnaire(View view) throws IOException {

        //declaracion de variables generales
        ArrayList questions = new ArrayList();
        ArrayList answerQuestions = new ArrayList();
        Boolean missOrNot = false;
        String missquestion = "";


        //save every option from radiogroup
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
        questions.add(R.id.radioGroup09);
        questions.add(R.id.radioGroup16);

        /*--------------------------------------------pregunta 3 y 10----------------------------------------------------------------*/

        for (int i = 0; i < questions.size(); i++) {
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            String[] questionNumber =  numberQuestion.get(i).split(" ");

            //si alguna opcion de radiogroup no fue selesccionada reinicia el cuestionario
            if(radioButtonGroup.getCheckedRadioButtonId() == -1)
            {
                missOrNot = true;
                missquestion = missquestion + "pregunta3, y 10 " + " \n";


            }else{
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);
                try {
                    questionnaireAnswers.edit().putInt(numberQuestion.get(i), indice).apply();
                } catch (Exception e) {
                    Log.d(TAG, "Questionnaire Error al llenar el cuestionario");
                }
            }
        }

        //crear los objetos a guardar.
        Object question12 = R.id.question12;
        Object question13 = R.id.question13;
        Object question14 = R.id.question14;
        Object question15 = R.id.question15;
        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_4,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};

        /*--------------------------------------------pregunta 1 ----------------------------------------------------------------*/
        int selectOrNot = 0;
        for (int i = 0 ; i < question07.length;i++){
            CheckBox box = findViewById((Integer) question07[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question07[i].toString(),true).apply();
                selectOrNot++;
            }
            else {
                questionnaireAnswers.edit().putBoolean(question07[i].toString(),false).apply();
            }
        }
        if (selectOrNot == 0){
            missOrNot = true;
            missquestion = missquestion +
                    "pregunta 1"+ " \n";
        }
        /*--------------------------------------------pregunta 2----------------------------------------------------------------*/

        selectOrNot=0;
        for (int i = 0 ; i < question08.length;i++){
            CheckBox box = findViewById((Integer) question08[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question08[i].toString(),true).apply();
                selectOrNot++;
            }
            else {
                questionnaireAnswers.edit().putBoolean(question08[i].toString(),false).apply();
            }
        }
        if (selectOrNot == 0){
            missOrNot = true;
            missquestion = missquestion +
                    "pregunta 2"+ " \n";
        }
        selectOrNot=0;

        /*--------------------------------------------pregunta 4----------------------------------------------------------------*/
        for (int i = 0 ; i < question10.length;i++){
            CheckBox box = findViewById((Integer) question10[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question10[i].toString(),true).apply();
                selectOrNot++;
            }
            else {
                questionnaireAnswers.edit().putBoolean(question10[i].toString(),false).apply();
            }
        }
        if (selectOrNot == 0){
            missOrNot = true;
            missquestion = missquestion +
                    "pregunta 4"+ " \n";
        }
        selectOrNot=0;

        /*--------------------------------------------pregunta 5----------------------------------------------------------------*/
        for (int i = 0 ; i < question11.length;i++){
            CheckBox box = findViewById((Integer) question11[i]);
            if(box.isChecked()){
                questionnaireAnswers.edit().putBoolean(question11[i].toString(),true).apply();
                selectOrNot++;
            }
            else {
                questionnaireAnswers.edit().putBoolean(question11[i].toString(),false).apply();
            }
        }

        if (selectOrNot == 0){
            missOrNot = true;
            missquestion = missquestion +
                    "pregunta 5"+ " \n";
        }

        /*--------------------------------------------pregunta 6----------------------------------------------------------------*/
        Integer question2  = 0;
        EditText valueOfQuestion = findViewById(R.id.question12);
        try {
            question2 = Integer.valueOf(valueOfQuestion.getText().toString());
        } catch (NumberFormatException e) {}

        if (question2 < 0) {
            questionnaireAnswers.edit().putString(question12.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 6"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question12.toString(), valueOfQuestion.getText().toString()).apply();
        }

        /*--------------------------------------------pregunta 7----------------------------------------------------------------*/
        question2  = 0;
        valueOfQuestion = findViewById(R.id.question13);
        try {
            question2 = Integer.valueOf(valueOfQuestion.getText().toString());
        } catch (NumberFormatException e) {}

        if (question2 < 0) {
            questionnaireAnswers.edit().putString(question13.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 7"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question13.toString(), valueOfQuestion.getText().toString()).apply();
        }
        /*--------------------------------------------pregunta 8----------------------------------------------------------------*/
        question2  = 0;
        valueOfQuestion = findViewById(R.id.question14);
        try {
            question2 = Integer.valueOf(valueOfQuestion.getText().toString());
        } catch (NumberFormatException e) {}

        if (question2 < 0) {
            questionnaireAnswers.edit().putString(question14.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 8"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question14.toString(), valueOfQuestion.getText().toString()).apply();
        }
        /*--------------------------------------------pregunta 9----------------------------------------------------------------*/
        question2  = 0;
        valueOfQuestion = findViewById(R.id.question15);
        try {
            question2 = Integer.valueOf(valueOfQuestion.getText().toString());
        } catch (NumberFormatException e) {}

        if (question2 < 0) {
            questionnaireAnswers.edit().putString(question15.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 9"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question15.toString(), valueOfQuestion.getText().toString()).apply();
        }

        //si hay algun error vuelve a presentar el cuestionario
        if(missOrNot){
            questionnaireAnswers.edit().putString("error", missquestion).apply();
            Intent intent1 = new Intent(getApplicationContext(),FinalQuestionnaire.class);
            startActivity(intent1);
            return;
        }

        //si se completo el cuestionario enviarlo a una pagina de agradecimiento y finalizar la app
        Intent intent = new Intent(getApplicationContext(), FinalPage.class);
        startActivity(intent);
        SendFile.senResFinalQues(getApplicationContext(), questionnaireAnswer());
        finish();
    }

    public String questionnaireAnswer(){

        SharedPreferences a = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        String respuesta = "";

        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup09);
        questions.add(R.id.radioGroup16);
        //set numberQuestion
        Object question12 = R.id.question12;
        Object question13 = R.id.question13;
        Object question14 = R.id.question14;
        Object question15 = R.id.question15;
        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_4,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};


        //Respuestas
        int value = 0;

        respuesta = respuesta + "\nPregunta 1.1: " + a.getBoolean(question07[0].toString(),false);
        respuesta = respuesta + "\nPregunta 1.2: " + a.getBoolean(question07[1].toString(),false);
        respuesta = respuesta + "\nPregunta 1.3: " + a.getBoolean(question07[2].toString(),false);
        respuesta = respuesta + "\nPregunta 1.4: " + a.getBoolean(question07[3].toString(),false);
        respuesta = respuesta + "\nPregunta 1.5: " + a.getBoolean(question07[4].toString(),false);
        respuesta = respuesta + "\nPregunta 1.6: " + a.getBoolean(question07[5].toString(),false);
        respuesta = respuesta + "\nPregunta 1.7: " + a.getBoolean(question07[6].toString(),false);

        respuesta = respuesta + "\nPregunta 2.1: " + a.getBoolean(question08[0].toString(),false);
        respuesta = respuesta + "\nPregunta 2.2: " + a.getBoolean(question08[1].toString(),false);
        respuesta = respuesta + "\nPregunta 2.3: " + a.getBoolean(question08[2].toString(),false);
        respuesta = respuesta + "\nPregunta 2.4: " + a.getBoolean(question08[3].toString(),false);
        respuesta = respuesta + "\nPregunta 2.5: " + a.getBoolean(question08[4].toString(),false);
        respuesta = respuesta + "\nPregunta 2.6: " + a.getBoolean(question08[5].toString(),false);
        respuesta = respuesta + "\nPregunta 2.7: " + a.getBoolean(question08[6].toString(),false);

        value = a.getInt(numberQuestion.get(0),-1);
        respuesta = respuesta + "\nPregunta 3: "+ Integer.toString(value);

        respuesta = respuesta + "\nPregunta 4.1: " + a.getBoolean(question10[0].toString(),false);
        respuesta = respuesta + "\nPregunta 4.2: " + a.getBoolean(question10[1].toString(),false);
        respuesta = respuesta + "\nPregunta 4.3: " + a.getBoolean(question10[2].toString(),false);
        respuesta = respuesta + "\nPregunta 4.4: " + a.getBoolean(question10[3].toString(),false);
        respuesta = respuesta + "\nPregunta 4.5: " + a.getBoolean(question10[4].toString(),false);
        respuesta = respuesta + "\nPregunta 4.6: " + a.getBoolean(question10[5].toString(),false);
        respuesta = respuesta + "\nPregunta 4.7: " + a.getBoolean(question10[6].toString(),false);
        respuesta = respuesta + "\nPregunta 4.8: " + a.getBoolean(question10[7].toString(),false);

        respuesta = respuesta + "\nPregunta 5.1: " + a.getBoolean(question11[0].toString(),false);
        respuesta = respuesta + "\nPregunta 5.2: " + a.getBoolean(question11[1].toString(),false);
        respuesta = respuesta + "\nPregunta 5.3: " + a.getBoolean(question11[2].toString(),false);

        respuesta = respuesta + "\nPregunta 6: " + a.getString(question12.toString(),"");
        respuesta = respuesta + "\nPregunta 7: " + a.getString(question13.toString(),"");
        respuesta = respuesta + "\nPregunta 8: " + a.getString(question14.toString(),"");
        respuesta = respuesta + "\nPregunta 9: " + a.getString(question15.toString(),"");

        value = a.getInt(numberQuestion.get(1),-1);
        respuesta = respuesta + "\nPregunta 10: "+ Integer.toString(value);
        return respuesta;
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

}

