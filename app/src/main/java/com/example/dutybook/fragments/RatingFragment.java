package com.example.dutybook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.dutybook.R;
import com.example.dutybook.adapters.RatingAdapter;
import com.example.dutybook.classes.Duty;
import com.example.dutybook.classes.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class RatingFragment extends Fragment {
    private final Double MAGIC_NUMBER = 47.8;
    static private ArrayList<Person> People = new ArrayList<>();
    private DatabaseReference myRef;
    private Spinner sp_how;
    private Spinner sp_grade;
    private ArrayList<Person> gradep;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        RecyclerView rv = view.findViewById(R.id.RecV_rating);
        sp_how = view.findViewById(R.id.sp_how);
        sp_grade = view.findViewById(R.id.sp_grade);
        final RatingAdapter adapter;
        adapter = new RatingAdapter (People);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinneradaptergrade = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.sp_grade_entries ,R.layout.spin_close);
        spinneradaptergrade.setDropDownViewResource(R.layout.spin_close);
        sp_grade.setAdapter(spinneradaptergrade);

        ArrayAdapter<CharSequence> spinneradapterhow = ArrayAdapter.createFromResource(getActivity(), R.array.sp_how_entries ,R.layout.spin_close);
        spinneradapterhow.setDropDownViewResource(R.layout.spin_close);
        sp_how.setAdapter(spinneradapterhow);

                sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        String[] items = getResources().getStringArray(R.array.sp_grade_entries);
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            ArrayList<Person> filterlist = new ArrayList<>();
                            for (int i = 0; i < People.size(); i++) {
                                if (People.get(i).getGrade().toLowerCase().contains(items[position]) && !filterlist.contains(People.get(i))) {
                                    filterlist.add(People.get(i));
                                }
                            }
                            sortrating(filterlist);
                            adapter.setPersonArrayList(filterlist);
                            adapter.notifyDataSetChanged();
                        }else{
                            sortrating(People);
                            adapter.setPersonArrayList(People);
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp_how.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    String[] items = getResources().getStringArray(R.array.sp_how_entries);
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(items[position].equals("Среди классов")) {
                            sp_how.setSelected(false);
                            sp_grade.setVisibility(View.INVISIBLE);
                            sp_grade.setEnabled(false);
                            String[] itemsgrade = getResources().getStringArray(R.array.sp_sub);
                            ArrayList<Person> people = new ArrayList<>();
                            for (String s : itemsgrade) {
                                int sum = 0;
                                for (int j = 0; j < People.size(); j++) {
                                    if (People.get(j).getGrade().equals(s)) {
                                        sum += People.get(j).getNumdelay();
                                    }
                                }
                                people.add(new Person(s, sum));
                            }

                            for (int i = 0; i < people.size(); i++) {
                                for (int j = 0; j < people.size(); j++) {
                                    if(people.get(i).getNumdelay() > people.get(j).getNumdelay()){
                                        Person temp = people.get(i);
                                        people.set(i,people.get(j));
                                        people.set(j,temp);
                                    }
                                }
                            }

                            adapter.setPersonArrayList(people);
                            adapter.notifyDataSetChanged();


                        }if(items[position].equals("Личный зачёт")){
                            sp_grade.setVisibility(View.VISIBLE);
                            sp_grade.setEnabled(true);
                            sortrating(People);
                            adapter.setPersonArrayList(People);
                            adapter.notifyDataSetChanged();
                        }if (items[position].equals("Рейтинг дежурств")){
                            sp_grade.setVisibility(View.INVISIBLE);
                            sp_grade.setEnabled(false);
                            gradep = new ArrayList<>();
                            myRef.child("dutyclasses").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Duty d = ds.getValue(Duty.class);
                                        Person p = new Person();
                                        assert d != null;
                                        p.setNumdelay((int) ((d.getAllrating() / d.getAllnum()) * MAGIC_NUMBER ));
                                        p.setName(d.getGrade());
                                        gradep.add(p);

                                    }
                                    adapter.setPersonArrayList(gradep);
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            }
                            );


                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });












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



    private void sortrating(ArrayList<Person> p){
        for (int i = 0; i < p.size(); i++) {
            for (int j = 0; j < p.size() ; j++) {
                if(p.get(i).getNumdelay() > p.get(j).getNumdelay()){
                    Person temp = p.get(i);
                    p.set(i,p.get(j));
                    p.set(j,temp);
                }
            }

        }

    }
        }
