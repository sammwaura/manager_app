package com.usalamatechnology.manageapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.models.PassengerDetails;

import java.util.ArrayList;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ViewHolder> {

    private ArrayList<PassengerDetails>passengerDetails;
    private Context context;





    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name_passenger, phone_no, seat_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name_passenger.findViewById(R.id.name_passenger);
            phone_no.findViewById(R.id.phone_passenger);
        }


    }

    public PassengerAdapter(ArrayList<PassengerDetails> passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.passengerdetail, viewGroup, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.name_passenger.setText(passengerDetails.get(i).passenger_name);
        viewHolder.phone_no.setText(passengerDetails.get(i).phone_no);



    }



    @Override
    public int getItemCount() {
        return passengerDetails.size();
    }

}
