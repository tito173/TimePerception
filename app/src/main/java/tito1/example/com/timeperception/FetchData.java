package tito1.example.com.timeperception;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FetchData extends AsyncTask<Void,Void,Void>  {
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    String user_id = "";
    Boolean quest = false;
    Boolean checkId = false;
    Boolean boolean1 = false;



    public FetchData(String string,Boolean b,String s) {
        if(s == "checkID")
            checkId = true;
        else if(s == "firstaccess")
            quest = true;
        user_id = string;

    }



    @Override
    protected Void doInBackground(Void... voids) {
        if (checkId){
            try {
                URL url = new URL("https://rarceresearch.fun:3034/user_check/" + user_id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line != null)
                        data = data + line;
                }
                Log.d("Prueba1", "response check: " + data);
                boolean1 = Boolean.valueOf(data);
                Log.d("Prueba1", "BOOLean: " + boolean1);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();}
        }else if(quest) {
            try {

                URL url = new URL("https://rarceresearch.fun:3034/initial/" + user_id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line != null)
                        data = data + line;
                }
                Log.d("Prueba1", "response initial: " + data);

//            Log.d("String",data);

//            JSONArray JA = new JSONArray(data);
//            for(int i =0 ;i <JA.length(); i++){
//                JSONObject JO = (JSONObject) JA.get(i);
//                singleParsed =  "Name:" + JO.get("name") + "\n"+
//                        "Password:" + JO.get("password") + "\n"+
//                        "Contact:" + JO.get("contact") + "\n"+
//                        "Country:" + JO.get("country") + "\n";
//
//                dataParsed = dataParsed + singleParsed +"\n" ;


//            }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

//        HomePage.data.setText(this.data);
        Questionnaire.bool = boolean1;


        /*Solo es una prueba*/



    }


}
