package tito1.example.com.timeperception;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
        finalMessage.setText(getString(R.string.fintest));
        finalMessage.setTextSize(30);
        finalMessage.setTextColor(Color.parseColor("#000000"));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}

