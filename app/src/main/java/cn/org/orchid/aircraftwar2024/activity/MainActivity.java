package cn.org.orchid.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.org.orchid.aircraftwar2024.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void beginOfflineGame(View source) {
        boolean sound;
        RadioButton radioButton = findViewById(R.id.sound_on_button);
        if(radioButton.isChecked()) {
            sound = true;
        } else {
            sound = false;
        }
        Intent intent = new Intent(MainActivity.this, OfflineActivity.class);
        intent.putExtra("sound",sound);
        startActivity(intent);
    }


    public void beginOnlineGame(View view) {
        Log.v("beginActivity","beginOnlineGame");
        boolean sound;
        RadioButton radioButton = findViewById(R.id.sound_on_button);
        if(radioButton.isChecked()) {
            sound = true;
        } else {
            sound = false;
        }
        Intent intent = new Intent(MainActivity.this, OnlineActivity.class);
        intent.putExtra("sound",sound);
        startActivity(intent);
    }
}