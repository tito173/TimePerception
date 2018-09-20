package tito1.example.com.timeperception;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

import static java.util.Arrays.asList;
/*Clase para cambiar el idioma de la app y futuras opciones de la app*/
public class Settings extends AppCompatActivity {

    private static final String TAG = "TP-Smart";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ArrayList<String> optionSelection = new ArrayList<String>(asList("English","Español","Greek"));

        //create presentation for the language
        ListView listView = findViewById(R.id.languagleListView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, optionSelection);
        listView.setAdapter(arrayAdapter);

        //make a listen to click event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new AlertDialog.Builder(Settings.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to change the language")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                                Resources res = getResources();
                                DisplayMetrics dm = res.getDisplayMetrics();
                                Locale myLocale;
                                Configuration conf = getResources().getConfiguration();
                                SharedPreferences idioma = getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);

                                //code to change the language
                                switch (i){
                                    case 0:
                                        Log.d(TAG,"case 0");
                                        myLocale = new Locale("en_US");
                                        conf.locale = myLocale;
                                        idioma.edit().putString("idoma","en_US").apply();
                                        break;
                                    case 1:
                                        Log.d(TAG,"case 1");
                                        myLocale = new Locale("es");
                                        conf.locale = myLocale;
                                        idioma.edit().putString("idoma","es").apply();
                                        break;
                                    default:
                                        Log.d(TAG, Integer.toString(i));
                                        myLocale = new Locale("en");
                                        conf.locale = myLocale;
                                        idioma.edit().putString("idoma","en").apply();
                                }
                                res.updateConfiguration(conf, dm);
//                                Intent refresh = new Intent(getApplicationContext(), HomePage.class);
//                                startActivity(refresh);
                                finish();

                            }

                        })
                        .setNegativeButton("No",null)
                        .show();

            }
        });
    }

}
