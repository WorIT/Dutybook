package com.example.dutybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Fragment launchFragment = new TodayUserFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments,launchFragment).commit();
        BottomNavigationView bnv_main = findViewById(R.id.nav_view);
        bnv_main.setSelectedItemId(R.id.bottomNavigationToday);

        BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.bottomNavigationRating:
                        fragment = new RatingFragment();
                        break;
                    case R.id.bottomNavigationToday:
                        fragment = new TodayUserFragment();
                        break;
                }
                assert fragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments,fragment).commit();
                return true;
            }
        };
        bnv_main.setOnNavigationItemSelectedListener(navlistener);
        ArrayList<Person> NotLatePeople = new ArrayList<>();
        Person person2 = new Person("Феликс Феликсович", "10-4", "17.02.2020", 7, 1);


    }
    @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.action_bar_menu, menu); return true; }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }}