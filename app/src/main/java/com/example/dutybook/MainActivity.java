package com.example.dutybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment launchFragment = new TodayFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments,launchFragment).commit();
        BottomNavigationView bnv_main = findViewById(R.id.nav_view);
        bnv_main.setSelectedItemId(R.id.bottomNavigationToday);
        BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.bottomNavigationLate:
                        fragment = new SetLateFragment();
                        break;
                    case R.id.bottomNavigationRating:
                        fragment = new RatingFragment();
                        break;
                    case R.id.bottomNavigationToday:
                        fragment = new TodayFragment();
                        break;
                }
                assert fragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments,fragment).commit();
                return true;
            }
        };
        bnv_main.setOnNavigationItemSelectedListener(navlistener);


    }
    @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.action_bar_menu, menu); return true; }
    @Override
public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }}