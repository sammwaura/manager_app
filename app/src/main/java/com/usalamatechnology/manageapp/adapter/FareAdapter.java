package com.usalamatechnology.manageapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usalamatechnology.manageapp.models.Faredetails;
import com.usalamatechnology.manageapp.R;

import java.util.ArrayList;
import java.util.List;

public class FareAdapter extends RecyclerView.Adapter <FareAdapter.ViewHolder> {
    private Context context;



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number_plate, amountCollected, passenger_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number_plate = itemView.findViewById(R.id.name_passenger);
            amountCollected = itemView.findViewById(R.id.amountCollected);
            passenger_no = itemView.findViewById(R.id.passenger_no);

        }
    }
    List<Faredetails> posts;

    public FareAdapter( Context context, List<Faredetails> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.cardfare, viewGroup, false);

        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.number_plate.setText(posts.get(i).getNumber_plate());
        viewHolder.amountCollected.setText(posts.get(i).getAmount());
        viewHolder.passenger_no.setText(posts.get(i).getPassenger_no());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
