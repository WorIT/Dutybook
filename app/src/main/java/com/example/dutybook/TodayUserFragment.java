package com.example.dutybook;

import android.annotation.SuppressLint;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


public class TodayUserFragment extends Fragment {
    private RatingBar rb_setuser;
    private RatingBar rb_now;
    private TextView tv_dutyclass;
    private static RecyclerView rv;
    private static VoiceUser v;
    private String dutygrade = "";
    private double dutyratingnow;
    private double numvoice;
    private double sumrating;
    private TextView tv_numdelayhistory;
    private DatabaseReference myRef;
    private TextView tv_nameuser;
    private Person personnow;
    private Integer personnowNumdelay;
    private HashMap<String,String> temp;
    private ArrayList<HistoryLate> lateHistory = new ArrayList<>();


    private ArrayList<HistoryLate> movedot(ArrayList<HistoryLate> hl){
        for (int i = 0; i < hl.size()  ; i++) {
            hl.get(i).setDate(hl.get(i).getDate().replace(',','.'));
        }
        return hl;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_today_user, container, false);
        rb_now = view.findViewById(R.id.rb_now);
        rv = view.findViewById(R.id.rv_historylate);
        tv_nameuser = view.findViewById(R.id.tv_nameuser);
        tv_numdelayhistory = view.findViewById(R.id.tv_numdelayHistory);
        tv_dutyclass = view.findViewById(R.id.tv_dutyclass);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        rb_setuser = view.findViewById(R.id.rb_setuser);
        final HistoryLateAdapter adapter = new HistoryLateAdapter (lateHistory);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("voiceuser").child(user.getUid()).child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personnow = dataSnapshot.getValue(Person.class);
                myRef.child("people").child(personnow.getName()).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        personnow = dataSnapshot.getValue(Person.class);
                        personnowNumdelay = personnow.numdelay;
                        tv_nameuser.setText(personnow.getName() + " " + personnow.getGrade());
                        tv_numdelayhistory.setText(Integer.toString(personnowNumdelay));
                        ArrayList<String> date = new ArrayList<>(personnow.delays.keySet());
                        date = sortdate(date);
                        ArrayList<HistoryLate> HistoryLateList = new ArrayList<>();
                        for (int i = 0; i < date.size() ; i++) {
                            HistoryLate h = new HistoryLate(date.get(i),personnow.delays.get(date.get(i)));
                            HistoryLateList.add(h);
                        }
                        movedot(HistoryLateList);
                        adapter.setHistoryLate(HistoryLateList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //VoiceUser r = new VoiceUser("19.03.2020",(float)3);
       // myRef.child("voiceuser").child(user.getUid()).setValue(r, new DatabaseReference.CompletionListener() {
       //    @Override
      //    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
      //      Toast.makeText(getActivity(),"Добавил)",Toast.LENGTH_SHORT).show();
      ///      }
      //   });


        myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Duty d = ds.getValue(Duty.class);
                    if(d.dutynow){
                        dutygrade = d.grade;
                        sumrating = d.rating;
                        numvoice = d.numvoice;
                        tv_dutyclass.setText("Сегодня дежурит " + dutygrade + " класс");
                        dutyratingnow = (double)d.rating / d.numvoice;
                        rb_now.setRating((float)dutyratingnow);
                        rb_now.setIsIndicator(true);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.child("voiceuser").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                v = dataSnapshot.getValue(VoiceUser.class);
                Date dateNow = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                String today = formatForDateNow.format(dateNow);
                if (v.datelast.equals(today)) {
                    rb_setuser.setRating(v.ratinglast);
                    rb_setuser.setIsIndicator(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rb_setuser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (user.getEmail() != null){
                    Date dateNow = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                    String today = formatForDateNow.format(dateNow);
                    if (!v.datelast.equals(today)) {
                        double newrating = sumrating + (double) rating;
                        double newnumvoice = numvoice + 1;
                        myRef.child("dutyclasses").child(dutygrade).child("rating").setValue(newrating);
                        myRef.child("dutyclasses").child(dutygrade).child("numvoice").setValue(newnumvoice);

                        rb_setuser.setIsIndicator(true);
                        Toast toast = Toast.makeText(getActivity(),
                                "Вы успешно оценили дежурство", Toast.LENGTH_SHORT);
                        toast.show();
                        myRef.child("voiceuser").child(user.getUid()).child("ratinglast").setValue((double) rating);
                        myRef.child("voiceuser").child(user.getUid()).child("datelast").setValue(today);
                    }

                }
            }
        });





        return view;

    }

    public ArrayList<String> sortdate(ArrayList<String> date){
        for (int i = 0; i < date.size(); i++) {
            for (int j = 0; j < date.size(); j++) {
                if(Integer.parseInt(date.get(i).substring(6,10)) > Integer.parseInt(date.get(j).substring(6,10))){
                    String q = date.get(i);
                    date.set(i,date.get(j));
                    date.set(j,q);
                }
            }
        }
        for (int i = 0; i < date.size(); i++) {
            for (int j = 0; j < date.size(); j++) {
                if(Integer.parseInt(date.get(i).substring(3,5)) > Integer.parseInt(date.get(j).substring(3,5)) && Integer.parseInt(date.get(i).substring(6,10)) >= Integer.parseInt(date.get(j).substring(6,10)) && Integer.parseInt(date.get(i).substring(6,10)) >= Integer.parseInt(date.get(j).substring(6,10))  ){
                    String q = date.get(i);
                    date.set(i,date.get(j));
                    date.set(j,q);
                }
            }
        }
        for (int i = 0; i < date.size(); i++) {
            for (int j = 0; j < date.size(); j++) {
                if(Integer.parseInt(date.get(i).substring(0,2)) > Integer.parseInt(date.get(j).substring(0,2)) && Integer.parseInt(date.get(i).substring(3,5)) >= Integer.parseInt(date.get(j).substring(3,5)) && Integer.parseInt(date.get(i).substring(6,10)) >= Integer.parseInt(date.get(j).substring(6,10))   ){
                    String q = date.get(i);
                    date.set(i,date.get(j));
                    date.set(j,q);
                }
            }
        }
        return date;
    }
}
