package tito1.example.com.timeperception;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class FinalPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        TextView finalMessage = (TextView) findViewById(R.id.finalText);
        finalMessage.setText("Final de la prueba, gracias por participar)");
        finalMessage.setTextSize(30);
        try {
            TimeUnit.SECONDS.sleep(3);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


//    public static void sendResultQuestDay(Context context)  {
//        SharedPreferences user_id = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
//        Object question07 = R.id.question07;
//        FetchData process = new FetchData(user_id.getString(question07.toString(),""),true,"questDay");
//        process.execute();
//
//    }
}

