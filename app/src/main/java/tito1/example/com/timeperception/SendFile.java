package tito1.example.com.timeperception;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
* Clase que crea el file y http POST, para luego ser enviado al servidor
*/
public class SendFile extends BroadcastReceiver {
    final String TAG ="TP-Smart";


    public static void fileProcessor(int cipherMode, String key, File inputFile, File outputFile) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        String key = "This is a secret";

        //Archivo de respuestas de cuestionario
        final File resCuestionario = new File(context.getExternalFilesDir(null),"RespuestasCuestionario.txt");


        final File testFile = new File(context.getExternalFilesDir(null), "EncrypFile.txt");
        final File testFileOrigin = new File(context.getExternalFilesDir(null), "TestFile.txt");
        Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,testFileOrigin,testFile);

        if (!testFile.exists()) {
            try {
                testFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //server where we will save every file of event
        final String url = "http://104.131.32.115/save_file.php";

        //thread to execute the process to send the file
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"SendFile Creado el archivo a enviar");


                //get the type of the file
                String content_type = getMimeType(testFile.getPath());

                //get the path, create http client, take the body content of file
                String file_path = testFile.getAbsolutePath();

                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), testFile);

                //form of date and time
                Object question07 = R.id.question07;
                SharedPreferences name = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
                String fileName = name.getString(question07.toString(),"NOID");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat1 = new SimpleDateFormat("HH-mm-ss");
                Date cal = Calendar.getInstance().getTime();
                String timeStamp = fileName +"-" +dateFormat.format(cal) + "-" + dateFormat1.format(cal);

                //combine the type and body
                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", content_type)
                        .addFormDataPart("uploaded_file", timeStamp + file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .build();

                //make the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(request_body)
                        .build();

                //send the file
                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG,"SendFile El archivo se envio al servidor");
                    SharedPreferences mensaje = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
                    mensaje.edit().putString("last",Calendar.getInstance().getTime().toString()).apply();
                    //response.body().close();

                    //if (name.getBoolean("firstLog",false) == true){
                    //Response response1 = client.newCall(request_installation).execute();
                    //name.edit().putBoolean("firstLog",false).apply();}

                    //if(name.getBoolean("appStopped",false)==true){
                    //Response response2 = client.newCall(request_installation).execute();
                    //name.edit().putBoolean("appStopped",false).apply();}

                    if (!response.isSuccessful() ) {
                        throw new IOException("Error of conection: " + response);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    }
            }
        });

        t.start();
    }

    //examine the type of the file
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            return type;

        }
        return type;
    }

    public static void SendResCuestionario(final Context context, final String string) throws IOException {

        final String TAG = "TP-Smart";

        String key = "This is a secret";
        final File respuesta = new File(context.getExternalFilesDir(null), "ResCuesEncryp.txt");
        final File archivoOriginal = new File(context.getExternalFilesDir(null), "ResCuestionario.txt");

        if (!respuesta.exists() || !archivoOriginal.exists()) {
            try {
                respuesta.createNewFile();
                archivoOriginal.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoOriginal, false /*append*/));
        writer.write(string);
        writer.close();
        Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,archivoOriginal,respuesta);

        //server where we will save every file of event
        final String url = "http://104.131.32.115/save_file.php";

        //thread to execute the process to send the file

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"SendFile Creado el archivo a enviar");


                //get the type of the file
                String content_type = getMimeType(respuesta.getPath());


                //get the path, create http client, take the body content of file
                String file_path = respuesta.getAbsolutePath();


                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), respuesta);

                //form of date and time
                Object question07 = R.id.question07;
                SharedPreferences name = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
                String fileName = name.getString(question07.toString(),"NOID");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat1 = new SimpleDateFormat("HH-mm-ss");
                Date cal = Calendar.getInstance().getTime();
                String timeStamp = fileName +"-" +dateFormat.format(cal) + "-" + dateFormat1.format(cal);

                //combine the type and body
                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", content_type)
                        .addFormDataPart("uploaded_file", timeStamp + file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .build();

                //make the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(request_body)
                        .build();

                //send the file
                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG,"SendFile El archivo Respuestas al servidor");

                    if (!response.isSuccessful() ) {
                        throw new IOException("Error of conection: " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            t.start();
        }
        else {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        SendResCuestionario( context, string);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Timer time = new Timer();
            time.schedule(timerTask,0,1000*60*10);


        }
    }


    public static void SendResPerseptionTest(final Context context,long l1,long l2,long l3,long l4) throws IOException {

        Log.d("Valores",""+l1+" "+l2+" "+l3+" "+l4+" ");
        final String TAG = "TP-Smart";

        String key = "This is a secret";
        final File respuesta = new File(context.getExternalFilesDir(null), "ResPerseptionEncryp.txt");
        final File archivoOriginal = new File(context.getExternalFilesDir(null), "ResPerseptionTest.txt");
        SharedPreferences questionnaireAnswers = context.getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);


        if (!respuesta.exists() || !archivoOriginal.exists()) {
            try {
                respuesta.createNewFile();
                archivoOriginal.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(archivoOriginal, true /*append*/));
        writer.write("Correct answer is: " + Long.toString(l1)+
                " the user select option: "+Integer.toString(questionnaireAnswers.getInt(
                "TestResp1", -1))+"\n");
        writer.write("Correct answer is: " + Long.toString(l2)+
                " the user select option: "+Integer.toString(questionnaireAnswers.getInt(
                "TestResp2", -1))+"\n");
        writer.write("Correct answer is: " + Long.toString(l3)+
                " the user select option: "+Integer.toString(questionnaireAnswers.getInt(
                "TestResp3", -1))+"\n");
        writer.write("Correct answer is: " + Long.toString(l4)+
                " the user select option: "+Integer.toString(questionnaireAnswers.getInt(
                "TestResp4", -1))+"\n");
        writer.close();
        Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,archivoOriginal,respuesta);

        //server where we will save every file of event
        final String url = "http://104.131.32.115/save_file.php";

        //thread to execute the process to send the file
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"SendFile Creado el archivo a enviar");


                //get the type of the file
                String content_type = getMimeType(respuesta.getPath());


                //get the path, create http client, take the body content of file
                String file_path = respuesta.getAbsolutePath();


                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), respuesta);

                //form of date and time
                Object question07 = R.id.question07;
                SharedPreferences name = context.getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
                String fileName = name.getString(question07.toString(),"NOID");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat1 = new SimpleDateFormat("HH-mm-ss");
                Date cal = Calendar.getInstance().getTime();
                String timeStamp = fileName +"-" +dateFormat.format(cal) + "-" + dateFormat1.format(cal);

                //combine the type and body
                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", content_type)
                        .addFormDataPart("uploaded_file", timeStamp + file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                        .build();

                //make the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(request_body)
                        .build();

                //send the file
                try {
                    Response response = client.newCall(request).execute();
                    Log.d(TAG,"SendFile El archivo Respuestas al servidor");

                    if (!response.isSuccessful() ) {
                        throw new IOException("Error of conection: " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

}




