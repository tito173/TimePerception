package com.timeperseption.TPSmart;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.timeperception.TPSmart.R;


public class FinalQuestionnaire extends AppCompatActivity {


    private final String TAG = "TP-Smart";
    ArrayList<String> numberQuestion = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_final_questionnaire);
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("com.timeperseption.TPSmart",Context.MODE_PRIVATE);

        SharedPreferences idioma = this.getSharedPreferences("com.timeperseption.TPSmart", Context.MODE_PRIVATE);

        //verifica que idioma tiene la app y mostrar el cuestionario en el idioma que corresponde
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

        /*Preparacion del cuestionario a mostrar*/
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

//        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};
//        for (int i = 0 ; i < question11.length;i++){
//            CheckBox box = findViewById((Integer) question11[i]);
//            box.setChecked(questionnaireAnswers.getBoolean(question11[i].toString(),false));
//        }

        Object[] question02 = {R.id.question12,R.id.question121,
                R.id.question13,R.id.question131,
                R.id.question14,R.id.question141,
                R.id.question15,R.id.question151};
        for (int i = 0 ; i< question02.length; i++){

            EditText valueOfQuestion = findViewById((Integer) question02[i]);
            valueOfQuestion.setText(questionnaireAnswers.getString(question02[i].toString(),""));
        }



    }

    //dejar que onResumen se encargue de la  presenetacion del cuestionario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_questionnaire);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    //guardas las opciones seleccionadas
    public void saveQuestionnaire(View view) throws IOException {

        //declaracion de variables generales
        ArrayList questions = new ArrayList();
        ArrayList answerQuestions = new ArrayList();
        Boolean missOrNot = false;
        String missquestion = "";


        //save every option from radiogroup
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("com.timeperseption.TPSmart",Context.MODE_PRIVATE);
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
                missquestion = missquestion + "pregunta3, y 9" + " \n";


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
        Object[] question12 = {R.id.question12,R.id.question121};
        Object[] question13 = {R.id.question13,R.id.question131};
        Object[] question14 = {R.id.question14,R.id.question141};
        Object[] question15 = {R.id.question15,R.id.question151};
        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_4,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
//        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};

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

        //omitida
        /*--------------------------------------------pregunta 5----------------------------------------------------------------*/
//        for (int i = 0 ; i < question11.length;i++){
//            CheckBox box = findViewById((Integer) question11[i]);
//            if(box.isChecked()){
//                questionnaireAnswers.edit().putBoolean(question11[i].toString(),true).apply();
//                selectOrNot++;
//            }
//            else {
//                questionnaireAnswers.edit().putBoolean(question11[i].toString(),false).apply();
//            }
//        }
//
//        if (selectOrNot == 0){
//            missOrNot = true;
//            missquestion = missquestion +
//                    "pregunta 5"+ " \n";
//        }

        /*--------------------------------------------pregunta 6----------------------------------------------------------------*/
        String question2  = "z";
        String question21  = "";
        EditText valueOfQuestion0 = findViewById(R.id.question12);
        EditText valueOfQuestion1 = findViewById(R.id.question121);

        question2 = valueOfQuestion0.getText().toString();
        question21 = valueOfQuestion1.getText().toString();

        if (question2.matches("") || question21.matches("") ) {
            questionnaireAnswers.edit().putString(question12[0].toString(), "").apply();
            questionnaireAnswers.edit().putString(question12[1].toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 5"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question12[0].toString(), valueOfQuestion0.getText().toString()).apply();
            questionnaireAnswers.edit().putString(question12[1].toString(), valueOfQuestion1.getText().toString()).apply();
        }

        /*--------------------------------------------pregunta 7----------------------------------------------------------------*/
        valueOfQuestion0 = findViewById(R.id.question13);
        valueOfQuestion1 = findViewById(R.id.question131);
        question2 = valueOfQuestion0.getText().toString();
        question21 = valueOfQuestion1.getText().toString();

        if (question2.matches("") || question21.matches("")) {
            questionnaireAnswers.edit().putString(question13[0].toString(), "").apply();
            questionnaireAnswers.edit().putString(question13[1].toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 6"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question13[0].toString(), valueOfQuestion0.getText().toString()).apply();
            questionnaireAnswers.edit().putString(question13[1].toString(), valueOfQuestion1.getText().toString()).apply();
        }
        /*--------------------------------------------pregunta 8----------------------------------------------------------------*/
        valueOfQuestion0 = findViewById(R.id.question14);
        valueOfQuestion1 = findViewById(R.id.question141);
        question2 = valueOfQuestion0.getText().toString();
        question21 = valueOfQuestion1.getText().toString();

        if (question2.matches("") || question21.matches("")) {
            questionnaireAnswers.edit().putString(question14[0].toString(), "").apply();
            questionnaireAnswers.edit().putString(question14[1].toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 7"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question14[0].toString(), valueOfQuestion0.getText().toString()).apply();
            questionnaireAnswers.edit().putString(question14[1].toString(), valueOfQuestion1.getText().toString()).apply();
        }
        /*--------------------------------------------pregunta 9----------------------------------------------------------------*/

        valueOfQuestion0 = findViewById(R.id.question15);
        valueOfQuestion1 = findViewById(R.id.question151);
        question2 = valueOfQuestion0.getText().toString();
        question21 = valueOfQuestion1.getText().toString();

        if (question2.matches("") || question21.matches("")) {
            questionnaireAnswers.edit().putString(question15[0].toString(), "").apply();
            questionnaireAnswers.edit().putString(question15[1].toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + "pregunta 8"+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question15[0].toString(), valueOfQuestion0.getText().toString()).apply();
            questionnaireAnswers.edit().putString(question15[1].toString(), valueOfQuestion1.getText().toString()).apply();
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
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        Settings.SendFile.senResFinalQues(getApplicationContext(), questionnaireAnswer());
        finish();

    }

    public String questionnaireAnswer(){

        SharedPreferences a = this.getSharedPreferences("com.timeperseption.TPSmart", Context.MODE_PRIVATE);
        String respuesta = "";

        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup09);
        questions.add(R.id.radioGroup16);
        //set numberQuestion
        Object[] question12 = {R.id.question12,R.id.question121};
        Object[] question13 = {R.id.question13,R.id.question131};
        Object[] question14 = {R.id.question14,R.id.question141};
        Object[] question15 = {R.id.question15,R.id.question151};
        Object[] question07 = {R.id.question7_1,R.id.question7_2,R.id.question7_3,R.id.question7_4,R.id.question7_5,R.id.question7_6,R.id.question7_7};
        Object[] question08 = {R.id.question8_1,R.id.question8_2,R.id.question8_3,R.id.question8_4,R.id.question8_5,R.id.question8_6,R.id.question8_7};
        Object[] question10 = {R.id.question10_1,R.id.question10_2,R.id.question10_3,R.id.question10_4,R.id.question10_5,R.id.question10_6,R.id.question10_7,R.id.question10_8};
//        Object[] question11 = {R.id.question11_1,R.id.question11_2,R.id.question11_3};

        ArrayList listRadioGroup = new ArrayList();
        for (int i = 0 ; i < questions.size() ; i++){
            RadioGroup radioG1 = findViewById((Integer) questions.get(i));
            RadioButton radioB1 = (RadioButton) radioG1.getChildAt(a.getInt(numberQuestion.get(i),-1));
            listRadioGroup.add(radioB1.getText());
        }

        //creacion del json para enviar a la base de datos
        String json = "{\"1\":["+a.getBoolean(question07[0].toString(),false)+","+
                a.getBoolean(question07[1].toString(),false)+","+
                a.getBoolean(question07[2].toString(),false)+","+
                a.getBoolean(question07[3].toString(),false)+","+
                a.getBoolean(question07[4].toString(),false)+","+
                a.getBoolean(question07[5].toString(),false)+","+
                a.getBoolean(question07[6].toString(),false)+"],"+
                "\"2\":["+a.getBoolean(question07[0].toString(),false)+","+
                a.getBoolean(question08[1].toString(),false)+","+
                a.getBoolean(question08[2].toString(),false)+","+
                a.getBoolean(question08[3].toString(),false)+","+
                a.getBoolean(question08[4].toString(),false)+","+
                a.getBoolean(question08[5].toString(),false)+","+
                a.getBoolean(question08[6].toString(),false)+"],"+
                "\"3\":\""+listRadioGroup.get(0)+"\", " +
                "\"4\":["+a.getBoolean(question07[0].toString(),false)+","+
                a.getBoolean(question10[1].toString(),false)+","+
                a.getBoolean(question10[2].toString(),false)+","+
                a.getBoolean(question10[3].toString(),false)+","+
                a.getBoolean(question10[4].toString(),false)+","+
                a.getBoolean(question10[5].toString(),false)+","+
                a.getBoolean(question10[6].toString(),false)+","+
                a.getBoolean(question10[7].toString(),false)+"],"+
//                "\"5\":["+a.getBoolean(question11[0].toString(),false)+","+
//                a.getBoolean(question11[1].toString(),false)+","+
//                a.getBoolean(question11[2].toString(),false)+"],"+
                "\"5\":["+a.getString(question12[0].toString(),"")+","+
                a.getString(question12[1].toString(),"")+"],"+
                "\"6\":["+a.getString(question13[0].toString(),"")+","+
                a.getString(question13[1].toString(),"")+"],"+
                "\"7\":["+a.getString(question14[0].toString(),"")+","+
                a.getString(question14[1].toString(),"")+"],"+
                "\"8\":["+a.getString(question15[0].toString(),"")+","+
                a.getString(question15[1].toString(),"")+"],"+
                "\"9\":\""+listRadioGroup.get(1)+"\"}";



        //Respuestas en el formato antiguo
//        int value = 0;
//
//        respuesta = respuesta + "Pregunta 1.1: " + a.getBoolean(question07[0].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.2: " + a.getBoolean(question07[1].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.3: " + a.getBoolean(question07[2].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.4: " + a.getBoolean(question07[3].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.5: " + a.getBoolean(question07[4].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.6: " + a.getBoolean(question07[5].toString(),false);
//        respuesta = respuesta + "\nPregunta 1.7: " + a.getBoolean(question07[6].toString(),false);
//
//        respuesta = respuesta + "\nPregunta 2.1: " + a.getBoolean(question08[0].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.2: " + a.getBoolean(question08[1].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.3: " + a.getBoolean(question08[2].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.4: " + a.getBoolean(question08[3].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.5: " + a.getBoolean(question08[4].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.6: " + a.getBoolean(question08[5].toString(),false);
//        respuesta = respuesta + "\nPregunta 2.7: " + a.getBoolean(question08[6].toString(),false);
//
//        value = a.getInt(numberQuestion.get(0),-1);
//        respuesta = respuesta + "\nPregunta 3: "+ Integer.toString(value);
//
//        respuesta = respuesta + "\nPregunta 4.1: " + a.getBoolean(question10[0].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.2: " + a.getBoolean(question10[1].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.3: " + a.getBoolean(question10[2].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.4: " + a.getBoolean(question10[3].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.5: " + a.getBoolean(question10[4].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.6: " + a.getBoolean(question10[5].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.7: " + a.getBoolean(question10[6].toString(),false);
//        respuesta = respuesta + "\nPregunta 4.8: " + a.getBoolean(question10[7].toString(),false);
//
//        respuesta = respuesta + "\nPregunta 5.1: " + a.getBoolean(question11[0].toString(),false);
//        respuesta = respuesta + "\nPregunta 5.2: " + a.getBoolean(question11[1].toString(),false);
//        respuesta = respuesta + "\nPregunta 5.3: " + a.getBoolean(question11[2].toString(),false);
//
//        respuesta = respuesta + "\nPregunta 6: " + a.getString(question12.toString(),"");
//        respuesta = respuesta + "\nPregunta 7: " + a.getString(question13.toString(),"");
//        respuesta = respuesta + "\nPregunta 8: " + a.getString(question14.toString(),"");
//        respuesta = respuesta + "\nPregunta 9: " + a.getString(question15.toString(),"");
//
//        value = a.getInt(numberQuestion.get(1),-1);
//        respuesta = respuesta + "\nPregunta 10: "+ Integer.toString(value);
//        Log.d(TAG, json.toString());
        return json;
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

