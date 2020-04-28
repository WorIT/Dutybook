package com.example.dutybook;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dutybook.classes.Duty;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TodayFragment extends Fragment {
    private DatabaseReference myRef;
    private FirebaseListAdapter mAdapter;
    private RatingBar rb;
    private TextView  tv_today_dutyclass;
    static private ArrayList<String> dutygrade = new ArrayList<>();
    private static double dutyrating;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_today, container, false);
        rb = view.findViewById(R.id.rb_duty);
        tv_today_dutyclass = view.findViewById(R.id.tv_today_dutyclass);
        lv = view.findViewById(R.id.lv_todayforteacher);
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Duty d = ds.getValue(Duty.class);
                    if (d.dutynow) {
                        dutygrade.add(d.grade);
                        dutyrating = (double) d.rating / d.numvoice;
                        rb.setRating((float) dutyrating);
                        rb.setIsIndicator(true);
                        tv_today_dutyclass.setText("Сегодня дежурит " + d.getGrade() + " класс");
                        mAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.comment, myRef.child("dutyclasses").child(dutygrade.get(0)).child("comments")) {
                            @Override
                            protected void populateView(View v, String s, int position) {
                                TextView textView = v.findViewById(R.id.tv_com);
                                textView.setText(s);
                            }
                        };
                        lv.setAdapter(mAdapter);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            }


        );





        return view;

}}
