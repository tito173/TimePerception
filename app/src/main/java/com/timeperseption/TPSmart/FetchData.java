package com.timeperseption.TPSmart;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void, Void, Void> {
    //variables para usar en comparaciones
    String TAG = "FetchData";
    static String data ="";
    String user_id = "";
    Boolean quest0 = false;
    Boolean checkId = false;
    Boolean noty = false;

    static Boolean boolean1;
    static Boolean timeconection;



    //optiene los datos para procesar
    public FetchData(String string,Boolean b,String s) {
        if(s == "checkID")
            checkId = true;
        else if(s == "firstaccess")
            quest0 = true;
        else if(s == "notificacion")
            noty = true;
//        else if(s == "questDay")
//            ques1_8 = true;
        user_id = string;

    }



    @Override
    protected Void doInBackground(Void... voids) {
        //verificac si existe el ID
        if (checkId) {
            try {
                data = "";
                //Verificar si el usuraio esta registrado
                Log.d(TAG + " CHECKID", "Verificando " + user_id);
                //crear el link con metodo get
                URL url = new URL("https://rarceresearch.fun:3034/user_check/" + user_id);
                //establecer la coneccion
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //varibale para determinar si occurio un timeout en la coneccion, indicando que hay problema con el servidor
                timeconection = true;
                //variable que idica la validez del usuario
                boolean1 = false;
                //seter el tiempo del timeout
                httpURLConnection.setConnectTimeout(5000);
                //optener la la respuesta
                InputStream inputStream = httpURLConnection.getInputStream();
                //procesar la respuesta
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line != null)
                        data = data + line;
                }
                Log.d("CHECKID", "response check: " + data);
                boolean1 = Boolean.valueOf(data);
                //varibale para determinar si occurio un timeout en la coneccion, si llego a este punto no occurio
                timeconection = false;
                checkId =false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (quest0) {
            try {
                data = "";
                //Proceso similar al anterior, esta vez para insetar un usuario valido a la base de datos
                URL url = new URL("https://rarceresearch.fun:3034/initial/" + user_id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int inputStream = httpURLConnection.getResponseCode();
                Log.d(TAG + "initial", "ResponseCode" + inputStream);
                quest0 = false;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Tarea para saber si se debe mostrar alguna notificacion al usuario.
        } else if (noty) {
            data = "";
            try {
                Log.d(TAG + "Notficaciones", "Verificando el usuario " + user_id);
                URL url = new URL("https://rarceresearch.fun:3034/status/" + user_id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                timeconection = true;
                httpURLConnection.setConnectTimeout(5000);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line != null)
                        data = data + line;
                }
                Log.d(TAG + "Notficaciones ", "data cruda: " + data);
                timeconection = false;
                noty = false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
            }
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        //Isentar codigo para una vez finalizado la tarea backgorud se realize esta.

    }



}
