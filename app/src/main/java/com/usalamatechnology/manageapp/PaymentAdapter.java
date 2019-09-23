package com.usalamatechnology.manageapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.ItemSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Paymentdetails> paymentdetails;
    public  PassengerDetailsIObserver mObserver;
    private OnItemCLickListener onItemCLickListener;


    private String numberplate_tapped;
    private String amount_tapped;

    public interface OnItemCLickListener{
        DrawerLayout getDrawer();

        void onItemClicked(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private  OnItemCLickListener onItemClickListener;
        public TextView number_plate, amountCollected, no_of_passenger, rate, destination;
        public CardView  cardtouch;



        public ViewHolder(@NonNull View itemView, OnItemCLickListener onItemCLickListener) {
            super(itemView);
            number_plate.findViewById(R.id.number_plate);
            amountCollected.findViewById(R.id.amountCollected);
//            no_of_passenger.findViewById(R.id.no_of_passengers);
//            rate.findViewById(R.id.rate);
            destination.findViewById(R.id.destination);
            cardtouch.findViewById(R.id.cardtouch);
            this.onItemClickListener = onItemCLickListener;
            itemView.setOnClickListener((View.OnClickListener) this);


        }

        @Override
        public void onClick(View v) {
            onItemCLickListener.onItemClicked(getAdapterPosition());
        }
    }



    public PaymentAdapter( Context context, ArrayList <Paymentdetails> paymentdetails, OnItemCLickListener onItemCLickListener){
        this.paymentdetails = paymentdetails;
        this.context = context;
        this.onItemCLickListener = onItemCLickListener;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.tripdetail, viewGroup, false);
        return new ViewHolder(view, onItemCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

    viewHolder.number_plate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.amountCollected.setText((CharSequence) paymentdetails.get(position));
        viewHolder.no_of_passenger.setText((CharSequence) paymentdetails.get(position));
        viewHolder.rate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.destination.setText((CharSequence) paymentdetails.get(position));

        numberplate_tapped = paymentdetails.get(position).getNumber_plate();
        amount_tapped = paymentdetails.get(position).getAmount();



        final int posi = position;

        viewHolder.cardtouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onCardClicked(posi, numberplate_tapped);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentdetails.size();
    }

}
