package com.example.dutybook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dutybook.classes.Duty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageTeacherFragment extends Fragment {
    private Button btn_send;
    private EditText messageteacher;
    private String message;
    private DatabaseReference myRef;
    static ArrayList<String> comments = new ArrayList<>();
    static private String dutygrade;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_teacher, container, false);
        btn_send = view.findViewById(R.id.btn_sendmessage);
        messageteacher = view.findViewById(R.id.newmessageteacher);
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Duty d = ds.getValue(Duty.class);
                    if(d.dutynow){
                        dutygrade = d.grade;
                        comments = d.comments;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }
        );

        messageteacher.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(messageteacher.getText().length() == 300) {
                    Toast.makeText(getActivity(), "Достигнута максимальная длина сообщения", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
            });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageteacher.getText().toString().equals("")) {
                    message = messageteacher.getText().toString();
                    messageteacher.setText("");
                    if (comments.get(0).equals("Сообщений нет")) {
                        ArrayList<String> newcomments = new ArrayList<>();
                        newcomments.add(message);
                        myRef.child("dutyclasses").child(dutygrade).child("comments").setValue(newcomments);
                       /// Toast.makeText(getActivity(), "Сообщение успешно отправлено", Toast.LENGTH_LONG).show();
                    } else {
                        comments.add(message);
                        myRef.child("dutyclasses").child(dutygrade).child("comments").setValue(comments);
                      ///  Toast.makeText(getActivity(), "Сообщение успешно отправлено", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return view;
    }
}
