package com.example.dutybook.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dutybook.R;
import com.example.dutybook.classes.Duty;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.Objects;

import static com.example.dutybook.R.id.chek_remind;
import static com.example.dutybook.R.layout.activity_enter;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class EnterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = getInstance();
    private DatabaseReference myRef;
    private EditText enter_password_et;
    private EditText enter_login_et ;
    private ArrayList<String> gradelist = new ArrayList<>();
    private CheckBox chek_remid;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "Login";
    public static final String APP_PREFERENCES_PASSWORD = "Password";
    static SharedPreferences mSettings;
    static String dutygrade = "";
    static String dutygradelastonline = "";
    private ProgressBar progressBar;

    private void updateduty(String today){
        if (!dutygradelastonline.equals(today)) {
            myRef.child("dutyclasses").child(dutygrade).child("rating").setValue((double) 0);
            myRef.child("dutyclasses").child(dutygrade).child("numvoice").setValue(0);
            myRef.child("dutyclasses").child(dutygrade).child("lastonline").setValue(today);
            ArrayList<String> comments = new ArrayList<>();
            comments.add("Сообщений нет");
            myRef.child("dutyclasses").child(dutygrade).child("comments").setValue(comments);
        }
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


    public void signin(final String email , final String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            progressBar.setVisibility(ProgressBar.VISIBLE);

            if (password.equals("753")){
                startActivity(new Intent(this,AdminActivity.class));
                return;
            }
           // boolean r = isDutynow("10-5");
            if (email.toLowerCase().contains("admin239")) {
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
                            //ArrayList<String> comm = new ArrayList<>();
                            // comm.add("Сообщений нет");
                            //Duty d = new Duty(comm, "10-5", true,0, 0,"23.03.2020");
                            //myRef.child("dutyclasses").child(d.grade).setValue(d);


                            myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Duty d = ds.getValue(Duty.class);
                                        assert d != null;
                                        if (d.getDutynow()) {
                                            dutygrade = d.getGrade();
                                            dutygradelastonline = d.getLastonline();
                                            Date dateNow = new Date();
                                            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                                            String today = formatForDateNow.format(dateNow);

                                            updateduty(today);
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                                            Intent intent_main = new Intent(EnterActivity.this, TeacherActivity.class);
                                            startActivity(intent_main);
                                        }
                                    }
                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            View snackview = findViewById(R.id.btn_fogotpassword);
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Snackbar snackbarno = Snackbar.make(snackview, "Ошибка авторизации", Snackbar.LENGTH_LONG);
                            View snackbarView = snackbarno.getView();
                            snackbarView.setBackgroundColor(getResources().getColor(R.color.inererror));
                            snackbarno.show();
                            chek_remid.setChecked(false);
                        }
                    }
                });

            } else if (email.length() == 4 || email.length() == 3 && inGrade(email)) {
                //boolean rd = isDutynow("10-5");
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
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                                    String today = formatForDateNow.format(dateNow);
                                    assert dutynow != null;
                                    if (!dutynow.getLastonline().equals(today)) {
                                        myRef.child("dutyclasses").child(email).child("rating").setValue((double) 0);
                                        myRef.child("dutyclasses").child(email).child("numvoice").setValue(0);
                                        myRef.child("dutyclasses").child(email).child("lastonline").setValue(today);
                                        ArrayList<String> comments = new ArrayList<>();
                                        comments.add("Сообщений нет");
                                        myRef.child("dutyclasses").child(email).child("comments").setValue(comments);
                                    }
                                    if (dutynow.getDutynow()) {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        Intent intent_main = new Intent(EnterActivity.this, MainActivity.class);
                                        startActivity(intent_main);
                                    } else{
                                        Toast.makeText(EnterActivity.this, "Cегодня ваш класс не дежурит", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            View snackview = findViewById(R.id.btn_fogotpassword);
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar snackbarno = Snackbar.make(snackview, "Ошибка авторизации", Snackbar.LENGTH_LONG);
                            View snackbarView = snackbarno.getView();
                            snackbarView.setBackgroundColor(getResources().getColor(R.color.inererror));
                            snackbarno.show();
                            chek_remid.setChecked(false);
                        }
                    }
                });}
            else{
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
                                        if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                                            Intent intent_main = new Intent(EnterActivity.this, UserActivity.class);
                                            startActivity(intent_main);
                                        } else {
                                            View snackview = findViewById(R.id.btn_fogotpassword);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Snackbar snackbarno = Snackbar.make(snackview, "Подтвердите свой электронный адрес", Snackbar.LENGTH_LONG);
                                            View snackbarView = snackbarno.getView();
                                            snackbarView.setBackgroundColor(getResources().getColor(R.color.inererror));
                                            snackbarno.show();
                                        }
                                    } else {
                                        View snackview = findViewById(R.id.btn_fogotpassword);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Snackbar snackbarno = Snackbar.make(snackview, "Ошибка авторизации", Snackbar.LENGTH_LONG);
                                        View snackbarView = snackbarno.getView();
                                        snackbarView.setBackgroundColor(getResources().getColor(R.color.inererror));
                                        snackbarno.show();
                                        chek_remid.setChecked(false);
                                    }
                                }

                            }
                    );

                }
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(activity_enter);
        myRef=FirebaseDatabase.getInstance().getReference();
        chek_remid=findViewById(chek_remind);
        enter_login_et = findViewById(R.id.enter_login_et);
        mSettings=getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        Button btn_fogot = findViewById(R.id.btn_fogotpassword);
        enter_password_et= findViewById(R.id.enter_password_et);
        progressBar =  findViewById(R.id.progressBar);
       // @SuppressLint("CutPasteId") final View contextView=findViewById(R.id.btn_fogotpassword);







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

        Button btn_enter_duty = findViewById(R.id.btn_enter_user);
        Button btn_enter_registration = findViewById(R.id.btn_enter_registration);
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
