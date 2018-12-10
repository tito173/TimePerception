package com.timeperseption.TPSmart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.timeperception.TPSmart.R;

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

