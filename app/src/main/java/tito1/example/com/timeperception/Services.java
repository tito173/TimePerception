package tito1.example.com.timeperception;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Arrays.asList;


/*Clase que registrad todos los eventos ocurridos en el celular*/
public  class Services extends AccessibilityService {
    //some variables
    final static private String TAG = "Service";
    final static private String language = Locale.getDefault().getLanguage();
    private NotificationManager notificationManager;
    private static final int ID_NOTIFCATION = 45612;

    //Know when the service start
    @Override
    public void onCreate() {
        Log.d(TAG,"Service Create");
        Log.d(TAG,language);

        //stop notification
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFCATION);


    }



    //Know when the service stop
    @Override
    public void onDestroy() {
        Log.d(TAG,"ERROR ----------");

        //patron de vibracion
        long vibrate[] = {0,100,100};

//        action of click notification
        Intent myIntent = new Intent(getApplicationContext(), AppStop.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                myIntent,
                0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentTitle("Accessibility Server stopped")
                .setContentText("Pleas reactivate the service")
                .setVibrate(vibrate)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());

        notificationManager.notify(ID_NOTIFCATION,builder.build());


        Log.d(TAG,"Service Destroy");
        saveEventStopApp("An error occurred and the app stopped");
        SharedPreferences name = getApplicationContext().getSharedPreferences("tito1.example.com.timeperception",Context.MODE_PRIVATE);
        name.edit().putBoolean("appStopped",true).apply();
    }

    //What to do, when the server connect
    @Override
    protected void onServiceConnected() {
        //Make a Accessibility variable to attach the necessary info
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
       |AccessibilityEvent.TYPE_VIEW_CLICKED;
         /*AccessibilityEvent.TYPE_VIEW_FOCUSED|

         If you only want this service to work with specific applications, set their
         package names here.  Otherwise, when the service is activated, it will listen
         to events from all applications.
        info.packageNames = new String[]
               {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};*/


        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated.  This service *is*
        // application-specific, so the flag isn't necessary.  If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);

    }

    //What to do, when occurred and event on the phone.
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //code copy from Android Developer
        final int eventType = event.getEventType();

        //switch case to know what is happen on the phone
        switch(eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //call eventCheck
                eventCheck(event);
                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                //call eventCheck
                eventCheck(event);
                break;}
//        if(eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
//            eventCheck(event);
//
//        }
//        if(eventType == AccessibilityEvent.TYPE_VIEW_CLICKED){
//            eventCheck(event);
//
//        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG,"Service Interrupt");
    }

    public void eventCheck(AccessibilityEvent event) {
    /*ver si se puede usar en ves de una lisat un set o un map*/
        //Array list of case that not matter check. for now only in spanish language.
        ArrayList<String> omitEvent = new ArrayList<String>(asList(
                "[]"));
        ArrayList<String> changeName = new ArrayList<String>(asList(
                "[Facebook]","[Instagram]","[Twitter]","[WhatsApp]"));

//        ArrayList<String> formatEvent = new ArrayList<>();

        //Creation of variable to save the lunch app on the service.
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("tito1.example.com.accessibilityservice", Context.MODE_PRIVATE);
        String eventText = "";


//        System.out.println("3."+event.getClassName());
//        System.out.println("4."+event.getPackageName());


        String currentApp = event.getText().toString();
//        if (!changeName.contains(currentApp)){
//            currentApp = "[Another App]";
//
//        }
        //        Log.d(TAG,"1"+currentApp.toString());

        String lastApp = sharedPreferences.getString("currentApp","");
//           Log.d(TAG,"2"+lastApp.toString());

        if(!omitEvent.contains(event.getText().toString()) && !currentApp.equals(lastApp)){
//            Log.d(TAG,lastApp.toString()+"-->"+currentApp.toString());
            Log.d(TAG,currentApp.toString());

            sharedPreferences.edit().putString("currentApp",currentApp.toString()).apply();

//            formatEvent.add(event.getText().toString());
//            formatEvent.add(event.getEventTime()+"");
//            formatEvent.add(Calendar.getInstance().getTime().toString());
            eventText = "";
            eventText = eventText + event.getText() + " " + event.getEventTime() + " "+ Calendar.getInstance().getTime();
//                  Toast.makeText(getApplicationContext(),eventText,Toast.LENGTH_SHORT).show();
//                    Log.d(TAG,eventText);
            saveEvent(eventText);
        }
//        System.out.println("1."+event.getEventType());
//        System.out.println("2."+event.getSource()+"\n");

        KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            Log.d("Pantalla","Esta bloqueada");}
            else {
            Log.d("Pantalla","No Esta bloqueada");
    }

    }

    public void saveEvent(String string){

        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            File testFile = new File(this.getExternalFilesDir(null), "TestFile.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
//          to eraise the content of file
//            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false ));
//            writer.write("");


            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(string);
            writer.newLine();
//
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException e) {
            Log.e("Test", "Unable to write to the TestFile.txt file.");
        }

    }
    public void saveEventStopApp(String string){

        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            File testFile = new File(this.getExternalFilesDir(null), "TPSmartStop.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
//          to eraise the content of file
//            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false ));
//            writer.write("");


            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(string);
            writer.newLine();
//
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException e) {
            Log.e("Test", "Unable to write to the TestFile.txt file.");
        }

    }
}
