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

public class FareAdapter extends RecyclerView.Adapter <FareAdapter.ViewHolder> {
    private Context context;

    private ArrayList <Faredetails> faredetails;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number_plate, amountCollected, passenger_no;
        public CardView cardtouch3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardtouch3.findViewById(R.id.cardtouch3);
            number_plate.findViewById(R.id.name_passenger);
            amountCollected.findViewById(R.id.amountCollected);
            passenger_no.findViewById(R.id.passenger_no);

        }
    }

    public FareAdapter(ArrayList<Faredetails> faredetails) {
        this.faredetails = faredetails;
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

        viewHolder.number_plate.setText(faredetails.get(i).getNumber_plate());

    }



    @Override
    public int getItemCount() {
        return faredetails.size();
    }


}
