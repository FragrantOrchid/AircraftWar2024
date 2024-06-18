package cn.org.orchid.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import cn.org.orchid.aircraftwar2024.R;

public class OfflineActivity extends AppCompatActivity {
    boolean sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        sound = getIntent().getBooleanExtra("sound",false);
    }
    public void beginEasyGame(View source) {
        Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
        intent.putExtra("sound",sound);
        intent.putExtra("gameType",1);
        startActivity(intent);
    }


    public void beginNormalGame(View view) {
        Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
        intent.putExtra("sound",sound);
        intent.putExtra("gameType",2);
        startActivity(intent);
    }

    public void beginHardGame(View view) {
        Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
        intent.putExtra("sound",sound);
        intent.putExtra("gameType",3);
        startActivity(intent);

    }


}