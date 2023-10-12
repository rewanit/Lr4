package com.example.lr4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.preference.SwitchPreference;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchMusic;
    private Switch switchSounds;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        switchMusic = findViewById(R.id.switchMusic);
        switchSounds = findViewById(R.id.switchSounds);

        boolean musicEnabled = preferences.getBoolean("music_enabled", true);
        boolean soundsEnabled = preferences.getBoolean("sounds_enabled", true);

        switchMusic.setChecked(musicEnabled);
        switchSounds.setChecked(soundsEnabled);

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("music_enabled", isChecked);
            editor.apply();
            boolean enabled = isChecked;

            MusicManager.setMusicEnabled(enabled);
            if (enabled) {
                MusicManager.startMusic();
            } else {
                MusicManager.stopMusic();
            }


        });

        switchSounds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("sounds_enabled", isChecked);
            editor.apply();
        });
    }
}