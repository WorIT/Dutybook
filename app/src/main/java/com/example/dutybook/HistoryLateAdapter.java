package com.example.dutybook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class HistoryLateAdapter extends RecyclerView.Adapter<HistoryLateAdapter.HistoryLateViewHolder> {
    ArrayList<HistoryLate> HistoryLate;

    public HistoryLateAdapter (ArrayList<HistoryLate> personArrayList){

        this.HistoryLate = personArrayList;
    }

    public class HistoryLateViewHolder extends RecyclerView.ViewHolder{
        TextView Datelate;
        TextView Timelate;
        public HistoryLateViewHolder(@NonNull View itemView) {
            super(itemView);
            Datelate = itemView.findViewById(R.id.tvDateHistoryPerson);
            Timelate = itemView.findViewById(R.id.tvTimeHistoryPerson);
        }
    }

    @NonNull
    @Override
    public HistoryLateViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historylate, parent, false);
        return new HistoryLateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryLateViewHolder holder, int position) {
        holder.Datelate.setText(HistoryLate.get(position).getDate());
        holder.Timelate.setText(HistoryLate.get(position).getTime());
    }
    public void setHistoryLate(ArrayList<HistoryLate> p){
        this.HistoryLate = p;
    }

    @Override
    public int getItemCount(){
        return HistoryLate.size();
    }

};

