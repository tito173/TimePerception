package tito1.example.com.timeperception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


//Cuestionario inicial de la app
public class Questionnaire extends AppCompatActivity {

    final String TAG = "TP-Smart";
    ArrayList<String> numberQuestion = new ArrayList<String>();
    static Boolean bool = false;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_questionnaire);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EditText other = findViewById(R.id.question01Other);

        //Errores al llenar el questionario, aparecera un textView con los errores
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);

        TextView error  = findViewById(R.id.error1);
        String fillError = questionnaireAnswers.getString("error","");
        if(fillError.equals(""))
            error.setVisibility(View.GONE);
        error.setText(fillError);
        error.setTextSize(20);
        error.setTextColor(Color.parseColor("#FF0040"));
        questionnaireAnswers.edit().remove("error").apply();

        //set numberQuestion
        numberQuestion.add("pregunta 1");
        numberQuestion.add("pregunta 3");
        numberQuestion.add("pregunta 5");
        numberQuestion.add("pregunta 6");

        //set the values saved
        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup01);
        questions.add(R.id.radioGroup03);
        questions.add(R.id.radioGroup05);
        questions.add(R.id.radioGroup06);

        //Para limpiar todas las respuestas del questionario descomentar
        //questionnaireAnswers.edit().clear().apply();

        //mark every radiogroup option selected
        for (int i = 0 ; i < questions.size(); i++){
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            int value = questionnaireAnswers.getInt(numberQuestion.get(i),-1);
            //RadioButton rb = (RadioButton) radioButtonGroup.getChildAt(value);
            try {
                ((RadioButton )radioButtonGroup.getChildAt(value)).setChecked(true);
                if (i==0 && value==3){
                    other.setText(questionnaireAnswers.getString("other",""));
                }
                else if (i==0 && value != 3){
                    other.setText("");
                }
            }
            catch (Exception  e){
                //Log.d(TAG,"Error when open question "+i);
            }
        }

        //mark evry box selected
        Object[] question04 = {R.id.question4_1,R.id.question4_2,R.id.question4_3,R.id.question4_4};
        for (int i = 0 ; i < question04.length;i++){
            CheckBox box = findViewById((Integer) question04[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question04[i].toString(),false));
        }

        //fill question 2 and question 7
        Object question02 = R.id.question02;
        EditText valueOfQuestion = findViewById(R.id.question02);
        valueOfQuestion.setText(questionnaireAnswers.getString(question02.toString(),""));
        Object question07 = R.id.question07;
        if(questionnaireAnswers.getBoolean("llenoCuestionario?", false)) {
            EditText valueOfQuestion07 = findViewById(R.id.question07);
            TextView textView7_0 = findViewById(R.id.quesiton7_0);
            valueOfQuestion07.setVisibility(View.GONE);
            textView7_0.setVisibility(View.GONE);
        }
        else {
            EditText valueOfQuestion07 = findViewById(R.id.question07);
            valueOfQuestion07.setText(questionnaireAnswers.getString(question07.toString(), ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


    }

    //Validate and save the user answers
    public  void SaveQuestionnaire(View view) throws IOException, InterruptedException, ExecutionException {


        EditText other = findViewById(R.id.question01Other);
        //create a list to contain the questions
        ArrayList questions = new ArrayList();

        //save every option from radiogroup
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        questions.add(R.id.radioGroup01);
        questions.add(R.id.radioGroup03);
        questions.add(R.id.radioGroup05);
        questions.add(R.id.radioGroup06);

        //flags for validation
        Boolean missOrNot = false;
        Boolean pregunta5 = false;

        //String to save the error and show to the user
        String missquestion = "";

        /*---------------------------------MULTIPLE CHOICE-------------------------------------------------------*/

        Object[] question04 = {R.id.question4_1, R.id.question4_2, R.id.question4_3, R.id.question4_4};
        int selectOrNot = 0;

        for (int i = 0; i < question04.length; i++) {
            CheckBox box = findViewById((Integer) question04[i]);
            if (box.isChecked()) {
                questionnaireAnswers.edit().putBoolean(question04[i].toString(), true).apply();
                selectOrNot++;
                if (i == 2){
                    pregunta5 = true;
                }

            } else {
                questionnaireAnswers.edit().putBoolean(question04[i].toString(), false).apply();
            }
        }
        if (selectOrNot == 0){
            missOrNot = true; missquestion = missquestion +
                    getString(R.string.Errorquesiton4)+ " \n";


        }
/*---------------------------------------------Radio Group--------------------------------------------------------*/
        for (int i = 0; i < questions.size(); i++) {
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            String[] questionNumber =  numberQuestion.get(i).split(" ");

            //si alguna opcion de radiogroup no fue selesccionada reinicia el cuestionario
            if(radioButtonGroup.getCheckedRadioButtonId() == -1)
            {
                missOrNot = true;
                if(i==2 && pregunta5){
                    missOrNot = false;
                }else {
                    missquestion = missquestion + getString(R.string.Errorquesiton1)+ " "+questionNumber[1] + " \n";
                }

            }else{
                int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
                View radioButton = radioButtonGroup.findViewById(radioButtonId);
                int indice = radioButtonGroup.indexOfChild(radioButton);
                try {
                    questionnaireAnswers.edit().putInt(numberQuestion.get(i), indice).apply();
                    if (i == 0 && indice == 3) {
                        questionnaireAnswers.edit().putString("other", other.getText().toString()).apply();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Questionnaire Error al llenar el cuestionario");
                }
            }
        }

/*----------------------------------------FILL QUESTION 2-----------------------------------------------*/
        Object question02 = R.id.question02;
        Integer question2  = 0;
        Integer question7 = 0;
        EditText valueOfQuestion = findViewById(R.id.question02);
        try {
            question2 = Integer.valueOf(valueOfQuestion.getText().toString());
        } catch (NumberFormatException e) {}

        if (question2 <= 10 || question2 >= 70 || question2 == null) {
            questionnaireAnswers.edit().putString(question02.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + getString(R.string.Errorquesiton2)+ "\n";
        } else {
            questionnaireAnswers.edit().putString(question02.toString(), valueOfQuestion.getText().toString()).apply();
        }

/*-------------------------------------------FILL QUESTION 7--------------------------------------------*/
        Object question07 = R.id.question07;
        EditText valueOfQuestion07 = findViewById(R.id.question07);
        try {
            question7 = Integer.valueOf(valueOfQuestion07.getText().toString());
        } catch (NumberFormatException e) {}

        //condicion previa para el proximo if
        //question7 <= 0 || question7 >= 100 || question7 == null
        if (question7 == 0||question7 == null) {
            questionnaireAnswers.edit().putString(question07.toString(), "").apply();
            missOrNot = true;
            missquestion = missquestion + getString(R.string.Errorquesiton7)+"\n";
        } else {
            questionnaireAnswers.edit().putString(question07.toString(), valueOfQuestion07.getText().toString()).apply();
            Log.d("ID1","Se guardo "+questionnaireAnswers.getString(question07.toString(), valueOfQuestion07.getText().toString()));
        }

        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);



        if(!checkId(getApplicationContext())){
            if(FetchData.timeconection){
                missOrNot = true;
                missquestion = getString(R.string.serverProblem);
            }else{
            missOrNot = true;
            missquestion += "ID invalido";
            }
        }
        //Si falta alguna contestacion reiniciar el cuestionario con las opciones previas
        if(missOrNot){
            questionnaireAnswers.edit().putString("error", missquestion).apply();
            Intent intent1 = new Intent(getApplicationContext(),Questionnaire.class);
            startActivity(intent1);
            return;
        }

        //set that the firt log are made
        firtlog.edit().putBoolean("llenoCuestionario?", true).apply();
        //al terminar el cuestionario comenzar la primera prueba de persepcion
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        firstAccess(getApplicationContext());
        SendTheLogs(getApplicationContext());
        SendFile.SendResCuestionario(this,questionnaireAnswer());
//        finish();
//        moveTaskToBack(true);
    }

    //pasar a un string con los resultados del cuestionario
    public String questionnaireAnswer(){

        SharedPreferences a = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        String respuesta = "";

        //set numberQuestion
        numberQuestion.add("pregunta 1");
        Object question02 = R.id.question02;
        numberQuestion.add("pregunta 3");
        Object[] question04 = {R.id.question4_1,R.id.question4_2,R.id.question4_3,R.id.question4_4};
        numberQuestion.add("pregunta 5");
        numberQuestion.add("pregunta 6");
        Object question07 = R.id.question07;

        //Respuestas
        int value = a.getInt(numberQuestion.get(0),-1);
        respuesta = respuesta + "Pregunta 1: "+ Integer.toString(value);

        respuesta = respuesta + "\nPregunta 2: " + a.getString(question02.toString(),"");

        value = a.getInt(numberQuestion.get(1),-1);
        respuesta = respuesta + "\nPregunta 3: "+ Integer.toString(value);

        respuesta = respuesta + "\nPregunta 4.1: " + a.getBoolean(question04[0].toString(),false);
        respuesta = respuesta + "\nPregunta 4.2: " + a.getBoolean(question04[1].toString(),false);
        respuesta = respuesta + "\nPregunta 4.3: " + a.getBoolean(question04[2].toString(),false);
        respuesta = respuesta + "\nPregunta 4.4: " + a.getBoolean(question04[3].toString(),false);

        value = a.getInt(numberQuestion.get(2),-1);
        respuesta = respuesta + "\nPregunta 5: "+ Integer.toString(value);

        value = a.getInt(numberQuestion.get(3),-1);
        respuesta = respuesta + "\nPregunta 6: "+ Integer.toString(value);

        respuesta = respuesta + "\nPregunta 7: " + a.getString(question07.toString(),"");

        return respuesta;
    }

    //funcion que repite el envio de los logs recopilados
    public static void SendTheLogs(Context context) {
        String TAG = "TP-Smart";

        //create new calendar instance
        Log.d(TAG,"Send the log func");

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //create a pending intent to be called at midnight
        Intent intent = new Intent(context,SendFile.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(context,0,intent,0);


        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, midnightPI);
//        SharedPreferences mensaje = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
//        mensaje.edit().putString("last","Question "+Calendar.getInstance().getTime().toString()).apply();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
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

    public static void firstAccess(Context context)  {
        SharedPreferences user_id = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        Object question07 = R.id.question07;
        FetchData process = new FetchData(user_id.getString(question07.toString(),""),true,"firstaccess");
        process.execute();

    }
    public static Boolean checkId(Context context) throws IOException, InterruptedException, ExecutionException {
        SharedPreferences user_id = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        Object question07 = R.id.question07;
        if(!user_id.getString(question07.toString(), "").equals("")){
            FetchData process = new FetchData(user_id.getString(question07.toString(),""),true,"checkID");
            process.execute().get();
            return FetchData.boolean1;
        }
//        TimeUnit.SECONDS.sleep(1);

        FetchData.timeconection = false;

        return false;



    }




}
