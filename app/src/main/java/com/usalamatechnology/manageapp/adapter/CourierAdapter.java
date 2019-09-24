package com.usalamatechnology.manageapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usalamatechnology.manageapp.models.Courierdetails;
import com.usalamatechnology.manageapp.R;

import java.util.ArrayList;

public class CourierAdapter extends RecyclerView.Adapter <CourierAdapter.ViewHolder>{

    private ArrayList<Courierdetails> courierdetails;
    private Context context;



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number_plate, amount, courier_id;
        public CardView cardtouch4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardtouch4.findViewById(R.id.cardtouch4);
            number_plate.findViewById(R.id.number_plate2);
            amount.findViewById(R.id.amountCollected2);
            courier_id.findViewById(R.id.courier_id);

        }
    }

    public CourierAdapter(ArrayList<Courierdetails> courierdetails) {
        this.courierdetails = courierdetails;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.cardcourier, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.number_plate.setText(courierdetails.get(i).getNumber_plate());
        viewHolder.amount.setText(courierdetails.get(i).getAmount());
        viewHolder.courier_id.setText(courierdetails.get(i).getCourier_id());

    }

    @Override
    public int getItemCount() {
        return courierdetails.size();
    }

}
