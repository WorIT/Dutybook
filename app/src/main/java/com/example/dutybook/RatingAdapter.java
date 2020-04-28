package com.example.dutybook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private ArrayList<Person> personArrayList;
    RatingAdapter(ArrayList<Person> personArrayList){
        this.personArrayList = personArrayList;
    }
    private int x = 0;

    static class RatingViewHolder extends RecyclerView.ViewHolder{
        TextView namerating;
        TextView graderating;
        TextView numdelayrating;
        RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            namerating = itemView.findViewById(R.id.tvNameRatingPerson);
            graderating = itemView.findViewById(R.id.tvGradeRatingPerson);
            numdelayrating = itemView.findViewById(R.id.tvRatingPersonNumdelay);
        }
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        holder.namerating.setText(personArrayList.get(position).getName());
        holder.graderating.setText(personArrayList.get(position).getGrade());
        holder.numdelayrating.setText(personArrayList.get(position).getNumdelayString());
    }
    public void setPersonArrayList(ArrayList<Person> p){
        this.personArrayList = p;
    }

    @Override
    public int getItemCount(){
        return personArrayList.size();
    }

};

