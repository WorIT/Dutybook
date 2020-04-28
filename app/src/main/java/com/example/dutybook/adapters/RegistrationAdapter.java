package com.example.dutybook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutybook.Person;
import com.example.dutybook.R;

import java.util.ArrayList;

class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder> implements Filterable{
    private ArrayList<Person> personArrayList;
    private ArrayList<Person> OrigList;
    private Context context;
    RegistrationAdapter(ArrayList<Person> personArrayList, Context context){
        this.OrigList = new ArrayList<>(personArrayList);
        this.personArrayList = personArrayList;
        this.context = context;
    }


    static class RegistrationViewHolder extends RecyclerView.ViewHolder{
        TextView namereg;
        TextView gradereg;
        RegistrationViewHolder(@NonNull View itemView) {
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

