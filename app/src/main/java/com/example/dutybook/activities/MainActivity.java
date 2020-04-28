package com.example.dutybook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.dutybook.R;
import com.example.dutybook.fragments.RatingFragment;
import com.example.dutybook.fragments.SetLateFragment;
import com.example.dutybook.fragments.TodayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment launchFragment = new TodayFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, launchFragment).commit();
        BottomNavigationView bnv_main = findViewById(R.id.nav_view);
        bnv_main.setSelectedItemId(R.id.bottomNavigationToday);
        BottomNavigationView.OnNavigationItemSelectedListener navlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.containerFragments, fragment).commit();
                return true;
            }
        };
        bnv_main.setOnNavigationItemSelectedListener(navlistener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}