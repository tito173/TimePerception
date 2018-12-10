package com.timeperseption.TPSmart;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class MyServiceIsRunning extends Service {

    Timer t = new Timer();

    public MyServiceIsRunning() {
//        try {
//            SetNotification.setAlarmPerseption(getBaseContext());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//            //para hacer pruebas una vez el cuestionario esta lleno y no volver a llenar otro.
//        SendTheLogs(getBaseContext());
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("MYSERVICE","Am I on Oncreate");
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(isMyServiceRunning(Service.class)){
                    Log.d("MYSERVICE","yes");
                }else {
                    Log.d("MYSERVICE","no");
                }
            }
        },1000,60*60*1000); // 1 hour between calls

    }

    @Override
    public void onDestroy() {
        t.cancel();
    }
}
