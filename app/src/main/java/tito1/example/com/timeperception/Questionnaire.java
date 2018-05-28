package tito1.example.com.timeperception;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;

/*
* Cuestionario de inicio de TPSmart
*/

public class Questionnaire extends AppCompatActivity {

    private final String TAG = "Questionnaire";
//    private static final String TAG = "test";
    ArrayList<String> numberQuestion = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EditText other = findViewById(R.id.question01Other);
        //set numberQuestion
        numberQuestion.add("question01");
        numberQuestion.add("question03");
        numberQuestion.add("question05");
        numberQuestion.add("question06");

        //set the values saved
        ArrayList questions = new ArrayList();
        questions.add(R.id.radioGroup01);
        questions.add(R.id.radioGroup03);
        questions.add(R.id.radioGroup05);
        questions.add(R.id.radioGroup06);

        //mark every radiogroup option selected
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
        for (int i = 0 ; i < questions.size(); i++){
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            int value = questionnaireAnswers.getInt(numberQuestion.get(i),-1);
            RadioButton rb = (RadioButton) radioButtonGroup.getChildAt(value);
            try {
                ((RadioButton )radioButtonGroup.getChildAt(value)).setChecked(true);
                if (i==0 && value==3){
                    other.setText(questionnaireAnswers.getString("other",""));
                }
                else if (i==0 && value != 3){
                    other.setText("");
                }
                Log.d(TAG,Integer.toString(value));
            }
            catch (Exception  e){
                Log.d(TAG,"Error when open"+i);
            }

        }
        Object[] question04 = {R.id.question4_1,R.id.question4_2,R.id.question4_3,R.id.question4_4};
        for (int i = 0 ; i < question04.length;i++){
            CheckBox box = findViewById((Integer) question04[i]);
            box.setChecked(questionnaireAnswers.getBoolean(question04[i].toString(),false));
        }

        Object question02 = R.id.question02;
        EditText valueOfQuestion = findViewById(R.id.question02);
        valueOfQuestion.setText(questionnaireAnswers.getString(question02.toString(),""));
        Object question07 = R.id.question07;
        EditText valueOfQuestion07 = findViewById(R.id.question07);
        valueOfQuestion07.setText(questionnaireAnswers.getString(question07.toString(),""));

    }
//    public void endQuestionnaire(View   view){
//        Intent intent = new Intent(getApplicationContext(), FinalQuestionnaire.class);
//        startActivity(intent);
//
//    }

    public  void SaveQuestionnaire(View view) {


        ArrayList questions = new ArrayList();
        EditText other = findViewById(R.id.question01Other);

        //to erase the data save
//        questions.clear();
        //save every option from radiogroup
        SharedPreferences questionnaireAnswers = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        questions.add(R.id.radioGroup01);
        questions.add(R.id.radioGroup03);
        questions.add(R.id.radioGroup05);
        questions.add(R.id.radioGroup06);

        for (int i = 0; i < questions.size(); i++) {
            RadioGroup radioButtonGroup = findViewById((Integer) questions.get(i));
            int radioButtonId = radioButtonGroup.getCheckedRadioButtonId();
            View radioButton = radioButtonGroup.findViewById(radioButtonId);
            int indice = radioButtonGroup.indexOfChild(radioButton);
            try {
                questionnaireAnswers.edit().putInt(numberQuestion.get(i), indice).commit();
                if (i == 0 && indice == 3) {
                    questionnaireAnswers.edit().putString("other", other.getText().toString()).commit();
                }
            } catch (Exception e) {
                Log.d(TAG, "Error");
            }
        }
        Object[] question04 = {R.id.question4_1, R.id.question4_2, R.id.question4_3, R.id.question4_4};


        for (int i = 0; i < question04.length; i++) {
            CheckBox box = findViewById((Integer) question04[i]);
            if (box.isChecked()) {
                questionnaireAnswers.edit().putBoolean(question04[i].toString(), true).commit();
            } else {
                questionnaireAnswers.edit().putBoolean(question04[i].toString(), false).commit();
            }
        }


        Object question02 = R.id.question02;
        EditText valueOfQuestion = findViewById(R.id.question02);
        if (valueOfQuestion == null) {
            questionnaireAnswers.edit().putString(question02.toString(), "").apply();

        } else {
            questionnaireAnswers.edit().putString(question02.toString(), valueOfQuestion.getText().toString()).apply();
        }

        Object question07 = R.id.question07;
        EditText valueOfQuestion07 = findViewById(R.id.question07);
        if (valueOfQuestion == null) {
            questionnaireAnswers.edit().putString(question07.toString(), "").apply();

        } else {
            questionnaireAnswers.edit().putString(question07.toString(), valueOfQuestion07.getText().toString()).apply();
        }


        //Send file ect...
        SharedPreferences firtlog = this.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        //set that the firt log are made
        firtlog.edit().putBoolean("firstLog", true).apply();

        //set the app was installed
        if (firtlog.getBoolean("appInstaled", true) == true) {
            //            false to no enter again.
            Log.d("Test", "Entre al if intalled");
            firtlog.edit().putBoolean("appInstaled", false).apply();
            Log.d("Test","function appWasInstaled");
//            Intent intent = new Intent(getApplicationContext(),AppWasInstaled.class);
//            startService(intent);
        } else {
            Log.d("Test", "Entre al else intalled");
        }


        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);

        SendTheLogs();

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


    //funcion que repite el envio de los logs recopilados
    public void SendTheLogs() {
        long minuto = 1000 * 60;
        long hora = minuto * 60;
        long dia = hora * 24;
        //create new calendar instance
        Log.d("Test","prepare el pendingintent");
        Calendar SendfileStart = Calendar.getInstance();

        //set the time to midnight tonight

        SendfileStart.set(Calendar.HOUR_OF_DAY, 0);
        SendfileStart.set(Calendar.MINUTE,0);
        SendfileStart.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        //create a pending intent to be called at midnight
        Intent intent = new Intent(getApplicationContext(),SendFile.class);
        PendingIntent midnightPI =  PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);


        am.setRepeating(AlarmManager.RTC_WAKEUP, SendfileStart.getTimeInMillis(), hora, midnightPI);

    }
}
