package com.example.lr4;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer backgroundMusicPlayer;
    private static boolean isMusicEnabled = true;

    public static void initialize(Context context) {
        if (backgroundMusicPlayer == null) {
            backgroundMusicPlayer = MediaPlayer.create(context, R.raw.bg_music);
            backgroundMusicPlayer.setLooping(true);
        }
    }

    public static void startMusic() {
        if (isMusicEnabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.start();
        }
    }

    public static void stopMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    public static void setMusicEnabled(boolean enabled) {
        isMusicEnabled = enabled;
    }

    public static boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public static void release() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }
}
