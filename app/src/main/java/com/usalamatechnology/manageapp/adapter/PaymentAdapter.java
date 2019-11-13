package com.usalamatechnology.manageapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usalamatechnology.manageapp.Interface.PaymentObserver;
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.models.Paymentdetails;
import com.usalamatechnology.manageapp.ui.PassengerActivity;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number_plate, rate,amount, passenger_no, destination, origin;
        public CardView  card;
        public LinearLayout touch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number_plate = itemView.findViewById(R.id.number_plate);
            rate = itemView.findViewById(R.id.rate);
            amount = itemView.findViewById(R.id.amountCollected);
            passenger_no =itemView.findViewById(R.id.no_passengers);
            destination = itemView.findViewById(R.id.destination);
            origin = itemView.findViewById(R.id.origin);
            card = itemView.findViewById(R.id.card);
            touch = itemView.findViewById(R.id.touch);
        }
    }

    List<Paymentdetails>posts;

    public PaymentAdapter(Context context, List <Paymentdetails> posts){
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
        final View view = LayoutInflater.from(context).inflate(R.layout.tripdetail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final Paymentdetails paymentdetail = posts.get(position);
        viewHolder.number_plate.setText(paymentdetail.getNumber_plate());
        viewHolder.rate.setText(paymentdetail.getRate());
        viewHolder.amount.setText(paymentdetail.getAmount());
        viewHolder.passenger_no.setText(paymentdetail.getPassenger_no());

        viewHolder.destination.setText(paymentdetail.getDestination());
        viewHolder.origin.setText(paymentdetail.getOrigin());

        viewHolder.touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PassengerActivity.class);
                String number_plate = paymentdetail.getNumber_plate();
                intent.putExtra("number_plate", number_plate);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
