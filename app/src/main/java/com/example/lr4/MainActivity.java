package com.example.lr4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean isMusicEnabled() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getBoolean("music_enabled", true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicManager.release();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ticTacButton = findViewById(R.id.TicTacButton);
        Button findPairButton = findViewById(R.id.FindPairButton);
        Button settingsButton = findViewById(R.id.SettingsButton);


        MusicManager.initialize(this);

        if (isMusicEnabled()) {
            MusicManager.startMusic();
        }



        ticTacButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
                startActivity(intent);
            }
        });

        findPairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindPairActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}