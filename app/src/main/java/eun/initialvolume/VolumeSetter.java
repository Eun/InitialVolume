package eun.initialvolume;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class VolumeSetter extends Service {

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setVolume();
        stopSelf();
    }

    public void setVolume() {
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (audioManager == null) {
            displayNotification("Unable to get AudioManager");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setStreamVolume(audioManager, AudioManager.STREAM_MUSIC, prefs, R.string.pref_stream_music, R.string.pref_stream_music_enabled);
        setStreamVolume(audioManager, AudioManager.STREAM_RING, prefs, R.string.pref_stream_ring, R.string.pref_stream_ring_enabled);
        setStreamVolume(audioManager, AudioManager.STREAM_ALARM, prefs, R.string.pref_stream_alarm, R.string.pref_stream_alarm_enabled);
        setStreamVolume(audioManager, AudioManager.STREAM_SYSTEM, prefs, R.string.pref_stream_system, R.string.pref_stream_system_enabled);
        setStreamVolume(audioManager, AudioManager.STREAM_NOTIFICATION, prefs, R.string.pref_stream_notification, R.string.pref_stream_notification_enabled);

    }

    private void setStreamVolume(AudioManager audioManager, int streamIndex, SharedPreferences prefs, int resVolumeId, int resEnabledId) {

        if (prefs.getBoolean(getString(resEnabledId), false) == true) {

            String prefString = getString(resVolumeId);
            try {
                int vol = prefs.getInt(prefString, 0);
                Log.i("InitialVolume", "settingVolume for " + prefString + " to " + vol );
                if (vol > -1 && vol < audioManager.getStreamMaxVolume(streamIndex)) {
                    audioManager.setStreamVolume(streamIndex, vol, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            } catch (Exception e) {
                displayNotification("Unable to set " + prefString);
            }
        }

    }

    private int notificationID = 0;
    private void displayNotification(String message) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            Toast.makeText(this, "InitialVolume: Unable to get NotificationManager", Toast.LENGTH_SHORT).show();
            return;
        }

        notificationManager.notify(notificationID, new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("InitialVolume").setContentText(message).build());
        notificationID++;
    }
}
