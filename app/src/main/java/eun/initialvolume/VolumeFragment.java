package eun.initialvolume;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SeekBarPreference;


public class VolumeFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_volume);
        AudioManager audioManager = (AudioManager)this.getContext().getSystemService(Context.AUDIO_SERVICE);

        setMax(audioManager, R.string.pref_cat_music, R.string.pref_stream_music, AudioManager.STREAM_MUSIC);
        setMax(audioManager, R.string.pref_cat_ring, R.string.pref_stream_ring, AudioManager.STREAM_RING);
        setMax(audioManager, R.string.pref_cat_alarm, R.string.pref_stream_alarm, AudioManager.STREAM_ALARM);
        setMax(audioManager, R.string.pref_cat_system, R.string.pref_stream_system, AudioManager.STREAM_SYSTEM);
        setMax(audioManager, R.string.pref_cat_notification, R.string.pref_stream_notification, AudioManager.STREAM_NOTIFICATION);
    }

    private void setMax(AudioManager audioManager, int catID, int key, int index) {
        PreferenceCategory cat = (PreferenceCategory)findPreference(getString(catID));
        ((SeekBarPreference)cat.findPreference(getString(key))).setMax(audioManager.getStreamMaxVolume(index));
    }
}