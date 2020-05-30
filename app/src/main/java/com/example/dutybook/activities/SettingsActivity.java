package com.example.dutybook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.dutybook.R;

public class SettingsActivity extends AppCompatActivity {
    Button btn_exit;
    Button feedback;
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

        feedback = findViewById(R.id.btn_say_dev);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:dutybook239@gmail.com") );
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dutybook");
                startActivity(Intent.createChooser(emailIntent, "Написать разработчику"));
            }
        });
    }
}
