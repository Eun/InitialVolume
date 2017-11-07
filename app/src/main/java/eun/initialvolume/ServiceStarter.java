package eun.initialvolume;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ServiceStarter extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs;
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                context.registerReceiver(this, filter);
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (prefs.getBoolean(context.getString(R.string.pref_set_on_boot), true)) {
                    Intent serviceIntent = new Intent(context, VolumeSetter.class);
                    context.startService(serviceIntent);
                }
                break;
            case Intent.ACTION_SCREEN_ON:
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (prefs.getBoolean(context.getString(R.string.pref_set_on_screen_on), false)) {
                    Intent serviceIntent = new Intent(context, VolumeSetter.class);
                    context.startService(serviceIntent);
                }
                break;
        }
    }
}
