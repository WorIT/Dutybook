package com.example.dutybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EnterActivity extends AppCompatActivity {
    private Button btn_enter_duty;
    private Button btn_enter_registration;
    private Button btn_fogot;
    static boolean isDuty;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private   FirebaseAuth.AuthStateListener mAuthListener;
    private EditText enter_password_et;
    private EditText enter_login_et;
    private ArrayList<String> gradelist = new ArrayList<>();
    private CheckBox chek_remid;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "Login";
    public static final String APP_PREFERENCES_PASSWORD = "Password";
    static SharedPreferences mSettings;
    static String dutygrade = "";
    static String dutygradelastonline = "";

    public boolean isDutynow(String s){
        myRef.child("dutyclasses").child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Duty dutynow = dataSnapshot.getValue(Duty.class);
                if(dutynow.dutynow){
                    isDuty = true;
                }
                else isDuty = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            return isDuty;
    }
    public boolean inGrade(String s){
        boolean t = false;
        for (int i = 0; i < gradelist.size(); i++) {
            if(gradelist.get(i).equals(s)){
                t = true;
            }
        }
        return t;
    }
    public String getGrade(String s){
        char [] c = s.toCharArray();
        String rez = "";
        for (int i = 0; i < c.length; i++) {
            if (c[i] != '@'){
                rez += c[i];
            }
            else return rez;

        }
        return rez;
    }
    public void signin(final String email , final String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            boolean r = isDutynow("10-5");
            if (email.contains("admin")) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (chek_remid.isChecked()) {
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString(APP_PREFERENCES_LOGIN, email);
                                editor.apply();
                                editor.putString(APP_PREFERENCES_PASSWORD, password);
                                editor.apply();


                            }
                            //ArrayList<String> comm = new ArrayList<>();
                            // comm.add("Сообщений нет");
                            //Duty d = new Duty(comm, "10-5", true,0, 0,"23.03.2020");
                            //myRef.child("dutyclasses").child(d.grade).setValue(d);


                            myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Duty d = ds.getValue(Duty.class);
                                        if (d.dutynow) {
                                            dutygrade = d.grade;
                                            dutygradelastonline = d.lastonline;
                                            Date dateNow = new Date();
                                            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                                            String today = formatForDateNow.format(dateNow);

                                            if (!dutygradelastonline.equals(today)) {
                                                myRef.child("dutyclasses").child(dutygrade).child("rating").setValue((double) 0);
                                                myRef.child("dutyclasses").child(dutygrade).child("numvoice").setValue(0);
                                                myRef.child("dutyclasses").child(dutygrade).child("lastonline").setValue(today);
                                                ArrayList<String> comments = new ArrayList<>();
                                                comments.add("Сообщений нет");
                                                myRef.child("dutyclasses").child(dutygrade).child("comments").setValue(comments);
                                            }
                                            Intent intent_main = new Intent(EnterActivity.this, TeacherActivity.class);
                                            startActivity(intent_main);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }else
                                Toast.makeText(EnterActivity.this, "Авторизация провалена", Toast.LENGTH_SHORT).show();
                        }
                });

            } else if (email.length() == 4 || email.length() == 3) {
                boolean rd = isDutynow("10-5");
                mAuth.signInWithEmailAndPassword(email + "@gmail.com", password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (chek_remid.isChecked()) {
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString(APP_PREFERENCES_LOGIN, email);
                                editor.apply();
                                editor.putString(APP_PREFERENCES_PASSWORD, password);
                                editor.apply();

                            }
                                myRef.child("dutyclasses").child(email).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Duty dutynow = dataSnapshot.getValue(Duty.class);
                                        Date dateNow = new Date();
                                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                                        String today = formatForDateNow.format(dateNow);
                                        if (!dutynow.lastonline.equals(today)) {
                                            myRef.child("dutyclasses").child(email).child("rating").setValue((double) 0);
                                            myRef.child("dutyclasses").child(email).child("numvoice").setValue(0);
                                            myRef.child("dutyclasses").child(email).child("lastonline").setValue(today);
                                            ArrayList<String> comments = new ArrayList<>();
                                            comments.add("Сообщений нет");
                                            myRef.child("dutyclasses").child(email).child("comments").setValue(comments);
                                        }
                                        if (dutynow.getDutynow()){
                                            Toast.makeText(EnterActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                                            Intent intent_main = new Intent(EnterActivity.this, MainActivity.class);
                                            startActivity(intent_main);
                                        }else  Toast.makeText(EnterActivity.this, "Cегодня ваш класс не дежурит", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                        } else
                            Toast.makeText(EnterActivity.this, "Авторизация провалена", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (chek_remid.isChecked()) {
                                        SharedPreferences.Editor editor = mSettings.edit();
                                        editor.putString(APP_PREFERENCES_LOGIN, email);
                                        editor.apply();
                                        editor.putString(APP_PREFERENCES_PASSWORD, password);
                                        editor.apply();

                                    }
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(EnterActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                                        Intent intent_main = new Intent(EnterActivity.this, UserActivity.class);
                                        startActivity(intent_main);
                                    } else
                                        Toast.makeText(EnterActivity.this, "Подтвердите свой электронный адрес", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EnterActivity.this, "Авторизация провалена", Toast.LENGTH_SHORT).show();
                                    chek_remid.setChecked(false);
                                }
                            }

                        }
                );

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        HashMap<String,String> d = new HashMap<>();
        d.put("23,02,2020","9:16");
        d.put("25,02,2020","9:18");
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        chek_remid = findViewById(R.id.chek_remind);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        enter_login_et = findViewById(R.id.enter_login_et);
        btn_fogot = findViewById(R.id.btn_fogotpassword);
        enter_password_et = findViewById(R.id.enter_password_et);

        ///Person person1 = new Person("Нечаев Игорь", "10-5", "31.02.2020", 7,false,d );
        ///Person person2 = new Person("Феликс Феликсович", "10-4", "23.02.2020", 45,false,d );
        //myRef.child("people").child(person1.getName()).setValue(person1);
        //myRef.child("people").child(person2.getName()).setValue(person2);
        if(mSettings.contains(APP_PREFERENCES_LOGIN) && mSettings.contains(APP_PREFERENCES_PASSWORD)) {
            chek_remid.setChecked(true);
            enter_login_et.setText(mSettings.getString(APP_PREFERENCES_LOGIN, ""));
            enter_password_et.setText(mSettings.getString(APP_PREFERENCES_PASSWORD, ""));
            signin(enter_login_et.getText().toString(),enter_password_et.getText().toString());

        }

        final Dialog dialog = new Dialog(EnterActivity.this);
        dialog.setTitle("Введите ваш адрес электронной почты");
        dialog.setContentView(R.layout.dialog_fogot);
        final EditText ef;
        final Button ban_sendmail;
        ef = dialog.findViewById(R.id.edtxt_emailfogot);
        ban_sendmail = dialog.findViewById(R.id.btn_senfogotemail);
        btn_fogot.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {
                                             dialog.show();
                                         }
                                     });
        ban_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String emailfogot = ef.getText().toString();
                    if (emailfogot.contains("@") ) {
                        mAuth.sendPasswordResetEmail(emailfogot).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EnterActivity.this, "Письмо со сбросом пароля отправлено", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        });
                    }
            }
        });
                gradelist.add("9-1");
                gradelist.add("9-2");
                gradelist.add("9-3");
                gradelist.add("9-4");
                gradelist.add("9-5");
                gradelist.add("9-6");
                gradelist.add("10-1");
                gradelist.add("10-2");
                gradelist.add("10-3");
                gradelist.add("10-4");
                gradelist.add("10-5");
                gradelist.add("10-6");
                gradelist.add("10-7");
                gradelist.add("11-2");
                gradelist.add("11-3");
                gradelist.add("11-4");
                gradelist.add("11-5");
                gradelist.add("11-6");
                gradelist.add("11-7");
                //mAuthListener = new FirebaseAuth.AuthStateListener() {
                //     @Override
                //     public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //     FirebaseUser user = firebaseAuth.getCurrentUser();
                //       if (user != null) {
                //            // добавить приветствие как у сбера
                //          Intent intent_main = new Intent(EnterActivity.this, MainActivity.class);
                //          startActivity(intent_main);
                //      } else {
                //         Toast toast = Toast.makeText(getApplicationContext(),
                //                "Вы не вошли", Toast.LENGTH_SHORT);
                //        toast.show();
                //     }
                //  }
                /// };


                btn_enter_duty = findViewById(R.id.btn_enter_user);
                btn_enter_registration = findViewById(R.id.btn_enter_registration);
                btn_enter_duty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signin(enter_login_et.getText().toString(), enter_password_et.getText().toString());
                    }
                });
                btn_enter_registration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_main = new Intent(EnterActivity.this, RegistrationActivity.class);
                        startActivity(intent_main);
                    }
                });

    }
}
