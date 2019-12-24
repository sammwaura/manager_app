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
import java.util.List;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.ViewHolder> {

    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name, phone_no, seat_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_passenger);
            seat_no = itemView.findViewById(R.id.seat_no);
            phone_no = itemView.findViewById(R.id.phone_passenger1);
        }


    }
    List<PassengerDetails> posts;

    public PassengerAdapter(Context context, List<PassengerDetails> posts) {
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
        final View view = LayoutInflater.from(context).inflate(R.layout.passengerdetail, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final PassengerDetails passengerDetail = posts.get(i);

        viewHolder.name.setText(passengerDetail.getName());
        viewHolder.seat_no.setText(passengerDetail.getSeat_no());
        viewHolder.phone_no.setText(passengerDetail.getPhone_no());


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
