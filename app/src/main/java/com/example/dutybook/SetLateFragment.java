package com.example.dutybook;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class SetLateFragment extends Fragment {
        private RecyclerView rv;

        private FirebaseAuth mAuth;
        private ArrayList<Person> NotLatePeople = new ArrayList<>();
        private DatabaseReference myRef;
        private SetLateAdapter adapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_set_late, container, false);
            SearchView sv;
            sv = view.findViewById(R.id.svSetLate);
            rv = view.findViewById(R.id.RecV_SetLate);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(layoutManager);

            myRef = FirebaseDatabase.getInstance().getReference();
            ///myRef.child("dutyclasses").child(d.grade).child("comments").child("3").push().setValue("Поиграть в кс");
            myRef.child("people").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<Person>> temp = new GenericTypeIndicator<ArrayList<Person>>() {};

                    NotLatePeople.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            Person p = ds.getValue(Person.class);
                            NotLatePeople.add(p);
                        }
                        Context context = getContext();
                    adapter = new SetLateAdapter(NotLatePeople,context);
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
          //myRef.child("dutyclasses").child(duty.grade).setValue(duty, new DatabaseReference.CompletionListener() {
           //   @Override
           //   public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
           ////       Toast.makeText(getActivity(),"Добавил)",Toast.LENGTH_SHORT).show();
           //    }
         //  });
            return view;
        }

}
