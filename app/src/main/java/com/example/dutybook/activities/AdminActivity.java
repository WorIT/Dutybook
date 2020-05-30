package com.example.dutybook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dutybook.R;
import com.example.dutybook.classes.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    Spinner sp_now;
    private FirebaseAuth mAuthReg;
    private DatabaseReference myRef;
    Spinner sp_newperson;
    Button add;
    EditText datanew;
    private String newgrade = "";
    private String newdutygrade = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sp_now = findViewById(R.id.spdutynowgrade);
        sp_newperson = findViewById(R.id.spnewperson);
        add = findViewById(R.id.bt_sendnewperson);
        datanew = findViewById(R.id.et_namenewperson);
        myRef = FirebaseDatabase.getInstance().getReference();
        ArrayAdapter<CharSequence> spinneradapterDutynow = ArrayAdapter.createFromResource(Objects.requireNonNull(this), R.array.sp_setdutynow, R.layout.spin_close);
        spinneradapterDutynow.setDropDownViewResource(R.layout.spin_close);
        sp_now.setAdapter(spinneradapterDutynow);

        ArrayAdapter<CharSequence> spinneradapternewPerson = ArrayAdapter.createFromResource(Objects.requireNonNull(this), R.array.sp_setdutynow, R.layout.spin_close);
        spinneradapterDutynow.setDropDownViewResource(R.layout.spin_close);
        sp_newperson.setAdapter(spinneradapterDutynow);

        sp_newperson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] items = getResources().getStringArray(R.array.sp_setdutynow);
                newgrade = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        sp_now.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] items = getResources().getStringArray(R.array.sp_setdutynow);
                newdutygrade = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person newPerson = new Person(datanew.getText().toString(), newgrade, "29.05.2020", 0, false, new HashMap<String, String>());
                myRef.child("people").child(newPerson.getName()).setValue(newPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        datanew.setText("");
                        Toast.makeText(getApplicationContext(), "Ученик добавлен в базу данных", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
