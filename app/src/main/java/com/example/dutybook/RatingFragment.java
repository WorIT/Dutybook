package com.example.dutybook;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RatingFragment extends Fragment {
    static private RecyclerView rv;
    static private ArrayList<Person> People = new ArrayList<>();
    private DatabaseReference myRef;
    private Spinner sp_how;
    private Spinner sp_grade;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
                rv = view.findViewById(R.id.RecV_rating);
                sp_how = view.findViewById(R.id.sp_how);
                sp_grade = view.findViewById(R.id.sp_grade);
        final RatingAdapter adapter = new RatingAdapter (People);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinneradaptergrade = ArrayAdapter.createFromResource(getActivity(), R.array.sp_grade_entries ,R.layout.spin_close);
        spinneradaptergrade.setDropDownViewResource(R.layout.spin_close);
        sp_grade.setAdapter(spinneradaptergrade);

        ArrayAdapter<CharSequence> spinneradapterhow = ArrayAdapter.createFromResource(getActivity(), R.array.sp_how_entries ,R.layout.spin_close);
        spinneradapterhow.setDropDownViewResource(R.layout.spin_close);
        sp_how.setAdapter(spinneradapterhow);

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("people").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                People.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Person p = ds.getValue(Person.class);
                    People.add(p);
                }
                adapter.setPersonArrayList(People);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
                return view;

}
        }
