package com.example.mobile_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_application.model.Sport;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<Sport> sports;
    int[] sportsImages = {R.drawable.ic_baseline_sports_football_24,
            R.drawable.ic_baseline_sports_basketball_24,
            R.drawable.ic_baseline_sports_cricket_24,
            R.drawable.ic_baseline_sports_mma_24,
            R.drawable.ic_baseline_sports_rugby_24,
            R.drawable.ic_baseline_sports_soccer_24};
    Context context;

    public CustomAdapter(Context context, ArrayList<Sport> sports) {
        this.context = context;
        this.sports = sports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(sports.get(position).getName());
        holder.image.setImageResource(sportsImages[position]);
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
        }
    }
}
