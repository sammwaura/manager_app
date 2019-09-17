package com.usalamatechnology.manageapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alexzh.circleimageview.ItemSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Paymentdetails> paymentdetails;
    private AdapterView.OnItemClickListener itemClickListener;
    public  PassengerDetailsIObserver mObserver;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number_plate, amountCollected, no_of_passenger, rate, destination;
        public CardView card_view;
        public ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            number_plate.findViewById(R.id.number_plate);
            amountCollected.findViewById(R.id.amountCollected);
            no_of_passenger.findViewById(R.id.no_of_passengers);
            rate.findViewById(R.id.rate);
            destination.findViewById(R.id.destination);
            card_view.findViewById(R.id.card_view);
            this.itemClickListener = itemClickListener;


        }
    }

    public void setListener(PassengerDetailsIObserver obs) { mObserver = obs; }


    public PaymentAdapter( ArrayList<Paymentdetails> paymentdetails){
        this.paymentdetails = paymentdetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.tripdetail, viewGroup, false);
        return new ViewHolder(view, (ItemClickListener) itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

    viewHolder.number_plate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.amountCollected.setText((CharSequence) paymentdetails.get(position));
        viewHolder.no_of_passenger.setText((CharSequence) paymentdetails.get(position));
        viewHolder.rate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.destination.setText((CharSequence) paymentdetails.get(position));

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("number_plate", paymentdetails.get(position).getNumber_plate());
                intent.putExtra("amountCollected", paymentdetails.get(position).getAmount());
                intent.putExtra("no_of_passengers", paymentdetails.get(position).getNo_of_passenger());
                intent.putExtra("rate", paymentdetails.get(position).getRate());
                intent.putExtra("destination", paymentdetails.get(position).getDestination());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return paymentdetails.size();
    }
//
//    @Override
//    public int getItemCount() {
//        return paymentdetails.size();
//    }

    public interface ItemClickListener {
        void onItemOnClick(View view, int position);

        View.OnClickListener onItemOnClick();
    }


}
