package com.example.dutybook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.dutybook.R;

public class SettingsActivity extends AppCompatActivity {
    Button btn_exit;
    public static final String APP_PREFERENCES = "mysettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                settings.edit().clear().apply();
                Intent intent = new Intent(SettingsActivity.this, EnterActivity.class);
                startActivity(intent);
            }
        });
    }
}
