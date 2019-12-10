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
import com.usalamatechnology.manageapp.ui.Courier;

import java.util.ArrayList;
import java.util.List;

public class CourierAdapter extends RecyclerView.Adapter <CourierAdapter.ViewHolder>{

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number_plate, amount, courier_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number_plate = itemView.findViewById(R.id.number_plateC);
            amount= itemView.findViewById(R.id.amountCollected2);
            courier_id = itemView.findViewById(R.id.courier_id);
        }
    }

    List<Courierdetails> posts;
    public CourierAdapter(Context context, List<Courierdetails> posts) {
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
        final View view = LayoutInflater.from(context).inflate(R.layout.cardcourier, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.number_plate.setText(posts.get(i).getNumber_plate());
        viewHolder.amount.setText(posts.get(i).getAmount());
        viewHolder.courier_id.setText(posts.get(i).getCourier_id());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
