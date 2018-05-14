package tito1.example.com.timeperception;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Toast.makeText(context,"Boot Complete",Toast.LENGTH_LONG).show();
            Log.d("Test","Boot Complete");
            Intent intent1 = new Intent(context,Services.class);
            context.startService(intent1);
        }
    }
}
