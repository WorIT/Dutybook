package com.example.dutybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth mAuthReg;
    EditText newemail;
    Button reg;
    private Person regPerson;
    private SearchView sv;
    private RecyclerView rv;
    EditText newpassword;
    private DatabaseReference myRef;
    static RegistrationAdapter adapter;
    static ArrayList<Person> People = new ArrayList<>();

    public void registration(String email, String password, final Person regPerson){
        if(!regPerson.auth) {
            myRef = FirebaseDatabase.getInstance().getReference();
            mAuthReg.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuthReg.getCurrentUser();
                                VoiceUser ry = new VoiceUser("19.03.2020", (float) 3);
                                myRef.child("people").child(regPerson.getName()).child("auth").setValue(true);
                                myRef.child("voiceuser").child(user.getUid()).setValue(ry);
                                myRef.child("voiceuser").child(user.getUid()).child("user").setValue(regPerson);
                                user.sendEmailVerification();
                                Toast.makeText(RegistrationActivity.this, "Подтвердите ваш электронный адрес", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrationActivity.this, EnterActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }else                    Toast.makeText(RegistrationActivity.this, "Учентая запись для " + regPerson.getName() + " " + regPerson.getGrade() + " создана", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthReg = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registration);
        newemail = findViewById(R.id.et_new_login);
        newpassword = findViewById(R.id.et_new_password);
        reg = findViewById(R.id.btn_registration);
        rv = findViewById(R.id.rv_reg);
        sv = findViewById(R.id.sv_reg);
        adapter = new RegistrationAdapter(People,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        myRef = FirebaseDatabase.getInstance().getReference();
        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.setContentView(R.layout.dialog_reg);
        final Button btn_sendmail;
        final TextView tv_regemail,tv_regperson;
        tv_regemail = dialog.findViewById(R.id.tv_regemail);
        tv_regperson = dialog.findViewById(R.id.reg_person);
        btn_sendmail = dialog.findViewById(R.id.btn_sendreg);
        myRef.child("people").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                People.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Person p = ds.getValue(Person.class);
                    People.add(p);
                }
                Context context = getApplicationContext();
                adapter = new RegistrationAdapter(People,context);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        btn_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(People.size() == 1)
                registration(newemail.getText().toString(),newpassword.getText().toString(),People.get(0));
                else Toast.makeText(RegistrationActivity.this,"Кликните на себя в списке",Toast.LENGTH_SHORT).show();
            }

        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(People.size() == 1) {
                    tv_regemail.setText(newemail.getText().toString());
                    tv_regperson.setText(People.get(0).getName() + " " + People.get(0).getGrade());
                    dialog.show();
                }else Toast.makeText(RegistrationActivity.this,"Кликните на себя в списке",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
