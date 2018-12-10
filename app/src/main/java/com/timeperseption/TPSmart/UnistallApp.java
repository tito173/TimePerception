package com.timeperseption.TPSmart;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.timeperception.TPSmart.R;

public class UnistallApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unistall_app);

        Intent intent=new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.timeperception.TPSmart"));
        startActivity(intent);
    }
}
