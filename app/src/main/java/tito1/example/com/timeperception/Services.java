package tito1.example.com.timeperception;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;


/*Clase que registrad todos los eventos ocurridos en el celular*/
public class Services extends AccessibilityService {

    //varibales
    final static private String TAG = "TP-Smart";
    final static private String language = Locale.getDefault().getLanguage();
    private NotificationManager notificationManager;
    private static final int ID_NOTIFCATION = 45612;


    private LocationManager locationManager;
    private LocationListener locationListener, locationListenerNetwork;
    private long minTime = 0;

    //Know when the service start
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        Log.d(TAG, "Service Create");
        Log.d(TAG, "Idioma del celular es: " + language);

        //stop notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFCATION);

        //geolocalizacion




    }

    public void onDestroy() {
//        Log.d(TAG, "Service onDestroy ERROR");

        //patron de vibracion
        long vibrate[] = {0, 100, 100};

        //action of click notification
        /* Intent myIntent = new Intent(getApplicationContext(), AppStop.class);notificationChannel vs NotificationManager
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

        notificationManager.notify(ID_NOTIFCATION, builder.build());
        */

        Log.d(TAG, "Service Destroy");
        saveEventStopApp("An error occurred and the app stopped");
        SharedPreferences name = getApplicationContext().getSharedPreferences("tito1.example.com.timeperception", Context.MODE_PRIVATE);
        name.edit().putBoolean("appStopped", true).apply();
    }

    //What to do, when the server connect
    @Override
    protected void onServiceConnected() {

        //Make a Accessibility variable to attach the necessary info
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        //| AccessibilityEvent.TYPE_VIEW_CLICKED;
         /*AccessibilityEvent.TYPE_VIEW_FOCUSED|

         If you only want this service to work with specific applications, set their
         package names here.  Otherwise, when the service is activated, it will listen
         to events from all applications.
        info.packageNames = new String[]
               {"com.motorola.launcher3", "com.instagram.android","com.facebook.katana"};
        */


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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //code copy from Android Developer
        final int eventType = event.getEventType();

        //switch case to know what is happen on the phone
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //call eventCheck
                eventCheck(event);
                break;
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                //call eventCheck
//                eventCheck(event);
//                break;
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service Interrupt");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void eventCheck(AccessibilityEvent event) {

        //Creation of variable to save the lunch app on the service.
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("tito1.example.com.accessibilityservice", Context.MODE_PRIVATE);
        String eventText = "";

//        event.getSource();
//        AccessibilityNodeInfo source = getRootInActiveWindow();
//
//        if (source!=null) {
//            eventText = source.getPackageName().toString() + " " + event.getEventTime() + " " + Calendar.getInstance().getTime();
//            Log.d(TAG, eventText);
//            saveEvent(eventText);
//        }

        AccessibilityNodeInfo source01 =  getRootInActiveWindow();
        if (source01 != null) {
            eventText = source01.getPackageName().toString() + " " + Long.toString(event.getEventTime()) + " "
                    + Calendar.getInstance().getTime().toString()+" "+ geolocalizacion();
            Log.d("EVENTO", eventText);
//            Log.d("EVENTO", geolocalizacion());
            saveEvent(eventText);
        }

    }
    @SuppressLint("MissingPermission")
    public String geolocalizacion (){
        String string = "";

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location", provider.toString() + " is new the provider");

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Location", provider.toString() + " is start");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Location", provider.toString() + " is stoped");

            }
        };

        locationListenerNetwork = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Location", provider.toString() + " is new the provider");

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Location", provider.toString() + " is start");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Location", provider.toString() + " is stoped");

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime, 0, locationListenerNetwork);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime, 0, locationListenerNetwork);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, 0, locationListener);
        Log.d("Location", "Else verification permition");

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String lattitude = "";
        String longitude = "";
        if (location != null) {
            Log.d("Location", "Localizacion de Network");
            double latti = location.getLatitude();
            double longi = location.getLongitude();
            lattitude = String.valueOf(latti);
            longitude = String.valueOf(longi);
       } else
        if (location1 != null) {
            Log.d("Location", "Localizacion de GPS");
            double latti = location1.getLatitude();
            double longi = location1.getLongitude();
            lattitude = String.valueOf(latti);
            longitude = String.valueOf(longi);
        }
        string = lattitude + " " + longitude;

        return string;
    }

    public void saveEvent(String string) {
        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            File testFile = new File(this.getExternalFilesDir(null), "TestFile.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            //to eraise the content of file uncomment the next two line
            //BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false ));
            //writer.write("");


            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(string);
            writer.newLine();

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

    public void saveEventStopApp(String string) {

        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.

            File testFile = new File(this.getExternalFilesDir(null), "TPSmartStop.txt");
            if (!testFile.exists())
                testFile.createNewFile();

            //to eraise the content of file uncomment the next two line
            //BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false ));
            //writer.write("");

            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(string);
            writer.newLine();

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
