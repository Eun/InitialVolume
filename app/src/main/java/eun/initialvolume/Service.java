package eun.initialvolume;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class Service extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs;
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                context.getApplicationContext().registerReceiver(this, filter);
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (prefs.getBoolean(context.getString(R.string.pref_set_on_boot), false)) {
                    setVolume(context);
                }
                break;
            case Intent.ACTION_SCREEN_ON:
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (prefs.getBoolean(context.getString(R.string.pref_set_on_screen_on), false)) {
                    setVolume(context);
                }
                break;
        }
    }

    public void setVolume(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            displayNotification(context,"Unable to get AudioManager");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        setStreamVolume(context, audioManager, AudioManager.STREAM_MUSIC, prefs, R.string.pref_stream_music, R.string.pref_stream_music_enabled);
        setStreamVolume(context, audioManager, AudioManager.STREAM_RING, prefs, R.string.pref_stream_ring, R.string.pref_stream_ring_enabled);
        setStreamVolume(context, audioManager, AudioManager.STREAM_ALARM, prefs, R.string.pref_stream_alarm, R.string.pref_stream_alarm_enabled);
        setStreamVolume(context, audioManager, AudioManager.STREAM_SYSTEM, prefs, R.string.pref_stream_system, R.string.pref_stream_system_enabled);
        setStreamVolume(context, audioManager, AudioManager.STREAM_NOTIFICATION, prefs, R.string.pref_stream_notification, R.string.pref_stream_notification_enabled);

    }

    private void setStreamVolume(Context context, AudioManager audioManager, int streamIndex, SharedPreferences prefs, int resVolumeId, int resEnabledId) {

        if (prefs.getBoolean(context.getString(resEnabledId), false) == true) {

            String prefString = context.getString(resVolumeId);
            try {
                int vol = prefs.getInt(prefString, 0);
                Log.i("InitialVolume", "settingVolume for " + prefString + " to " + vol );
                if (vol > -1 && vol < audioManager.getStreamMaxVolume(streamIndex)) {
                    audioManager.setStreamVolume(streamIndex, vol, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            } catch (Exception e) {
                displayNotification(context, "Unable to set " + prefString);
            }
        }

    }

    private int notificationID = 0;
    private void displayNotification(Context context, String message) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Toast.makeText(context, "InitialVolume: Unable to get NotificationManager", Toast.LENGTH_SHORT).show();
            return;
        }

        notificationManager.notify(notificationID, new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("InitialVolume").setContentText(message).build());
        notificationID++;
    }
}
