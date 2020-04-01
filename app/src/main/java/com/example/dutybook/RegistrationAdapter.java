package com.example.dutybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder> implements Filterable{
    private ArrayList<Person> personArrayList = new ArrayList<>();
    private ArrayList<Person> OrigList = new ArrayList<>();
    Context context;
    public RegistrationAdapter (ArrayList<Person> personArrayList,Context context){
        this.OrigList = new ArrayList<>(personArrayList);
        this.personArrayList = personArrayList;
        this.context = context;
    }


    public class RegistrationViewHolder extends RecyclerView.ViewHolder{
        TextView namereg;
        TextView gradereg;
        public RegistrationViewHolder(@NonNull View itemView) {
            super(itemView);
            namereg = itemView.findViewById(R.id.tvNameRegPerson);
            gradereg = itemView.findViewById(R.id.tvGradeRegPerson);

        }
    }

    @NonNull
    @Override
    public RegistrationViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);
        return new RegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RegistrationViewHolder holder, final int position) {
        holder.namereg.setText(personArrayList.get(position).getName());
        holder.gradereg.setText(personArrayList.get(position).getGrade());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef;
                Date dateNow = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");

                String today = formatForDateNow.format(dateNow);
                myRef = FirebaseDatabase.getInstance().getReference();
                Person regPerson = personArrayList.get(position);
                getFilter().filter(regPerson.getName() + " " + regPerson.getGrade());
                Toast.makeText(context, "Если вы " + regPerson.getName() + " " + regPerson.getGrade() + " продолжите регистрацию", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount(){
        return personArrayList.size();
    }

    public Filter getFilter(){
        return examplefilter;
    }

    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Person> filterlist = new ArrayList<>();
            if(constraint == null || constraint.length() == 0 ){
                filterlist.addAll(OrigList);
            }else{
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(Person person : OrigList){
                    if((person.getName() + " " +  person.getGrade()).toLowerCase().contains(filterpattern)){
                        filterlist.add(person);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            personArrayList.clear();
            personArrayList.addAll((ArrayList<Person>)results.values);
            notifyDataSetChanged();


        }

    };
}

