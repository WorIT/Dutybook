package com.example.dutybook;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class SetLateAdapter extends RecyclerView.Adapter<SetLateAdapter.SetLateViewHolder> implements Filterable{
    private ArrayList<Person> personArrayList;
    private ArrayList<Person> OrigList;
    private Context context;
    public SetLateAdapter (ArrayList<Person> personArrayList,Context context){
        this.OrigList = new ArrayList<>(personArrayList);
        this.personArrayList = personArrayList;
        this.context = context;
    }


    public class SetLateViewHolder extends RecyclerView.ViewHolder{
        TextView namesetlate;
        TextView gradesetlate;
        public SetLateViewHolder(@NonNull View itemView) {
            super(itemView);
            namesetlate = itemView.findViewById(R.id.tvNameSetLate);
            gradesetlate = itemView.findViewById(R.id.tvGradeSetLate);

        }
    }

    @NonNull
    @Override
    public SetLateViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setlate, parent, false);
        return new SetLateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SetLateViewHolder holder, final int position) {
        holder.namesetlate.setText(personArrayList.get(position).getName());
        holder.gradesetlate.setText(personArrayList.get(position).getGrade());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef;
                Date dateNow = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                String today = formatForDateNow.format(dateNow);
                String today_hashmap = today.replace('.',',');
                SimpleDateFormat formattime = new SimpleDateFormat("hh:mm:ss");
                String timenow = formattime.format(dateNow);
                myRef = FirebaseDatabase.getInstance().getReference();
                HashMap<String, String> temp = new HashMap<>();
                if (!personArrayList.get(position).getDatelast().equals(today)){
                    if(personArrayList.get(position).delays != null) {
                        temp = personArrayList.get(position).delays;
                        temp.put(today_hashmap, timenow);
                    }else
                    {
                        temp.put(today_hashmap, timenow);
                    }
                    Toast.makeText(SetLateAdapter.this.context,"Опоздание зафиксированно",Toast.LENGTH_SHORT).show();
                    myRef.child("people").child(personArrayList.get(position).getName()).child("numdelay").setValue(personArrayList.get(position).getNumdelay() + 1);
                    myRef.child("people").child(personArrayList.get(position).getName()).child("datelast").setValue(today);
                    myRef.child("people").child(personArrayList.get(position).getName()).child("delays").setValue(temp);
                }else Toast.makeText(context, "Опоздание этого ученика уже зафиксированно", Toast.LENGTH_SHORT).show();
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
                    if((person.getName() + person.getGrade()).toLowerCase().contains(filterpattern)){
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

