package tito1.example.com.timeperception;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        Crypto crypto = new Crypto();
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
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"SendFile Creado el archivo a enviar");


                //get the type of the file
                String content_type = getMimeType(testFile.getPath());
                //String content_type_installation = getMimeType(fileIntallation.getPath());
                //String content_type_appStopped = getMimeType(fileIntallation.getPath());

                //get the path, create http client, take the body content of file
                String file_path = testFile.getAbsolutePath();
                //String fiel_path_intallation = fileIntallation.getAbsolutePath();
                //String fiel_path_appStopped = appstopped.getAbsolutePath();

                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type), testFile);
                //RequestBody file_body_intallation = RequestBody.create(MediaType.parse(content_type_installation),fileIntallation);
                //RequestBody file_body_appStopped = RequestBody.create(MediaType.parse(content_type_appStopped),appstopped);

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
                //RequestBody request_body_intallation = new MultipartBody.Builder()
                    //.setType(MultipartBody.FORM)
                    //.addFormDataPart("type", content_type_installation)
                    //.addFormDataPart("uploaded_file", timeStamp + fiel_path_intallation.substring(fiel_path_intallation.lastIndexOf("/") + 1), file_body_intallation)
                    //.build();

                //RequestBody request_body_appStoped = new MultipartBody.Builder()
                    //.setType(MultipartBody.FORM)
                    //.addFormDataPart("type", content_type_appStopped)
                    //.addFormDataPart("uploaded_file", timeStamp + fiel_path_appStopped.substring(fiel_path_appStopped.lastIndexOf("/") + 1), file_body_intallation)
                    //.build();

                //make the request
                Request request = new Request.Builder()
                        .url(url)
                        .post(request_body)
                        .build();

                //Request request_installation = new Request.Builder()
                    //.url(url)
                    //.post(request_body_intallation)
                    //.build();

                //Request request_appStopped = new Request.Builder()
                    //.url(url)
                    //.post(request_body_appStoped)
                    //.build();

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
}




