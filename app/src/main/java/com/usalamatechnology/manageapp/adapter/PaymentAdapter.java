package com.usalamatechnology.manageapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usalamatechnology.manageapp.Interface.PaymentObserver;
import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.models.Paymentdetails;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Paymentdetails> paymentdetails;
    public PaymentObserver mObserver;


    private String numberplate_tapped;
    private String amount_tapped;



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number_plate, amountCollected, no_of_passenger, rate, destination;
        public CardView  card;
        public LinearLayout touch;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number_plate = itemView.findViewById(R.id.number_plate);
            amountCollected = itemView.findViewById(R.id.amountCollected);
            destination = itemView.findViewById(R.id.destination);
            card = itemView.findViewById(R.id.card);
            touch = itemView.findViewById(R.id.touch);

        }


    }

    public PaymentAdapter(Context context, PaymentObserver observer, ArrayList <Paymentdetails> paymentdetails){
        this.context = context;
        this.mObserver = observer;
        this.paymentdetails = paymentdetails;


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

    viewHolder.number_plate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.amountCollected.setText((CharSequence) paymentdetails.get(position));
        viewHolder.no_of_passenger.setText((CharSequence) paymentdetails.get(position));
        viewHolder.rate.setText((CharSequence) paymentdetails.get(position));
        viewHolder.destination.setText((CharSequence) paymentdetails.get(position));



        final int posi = position;

        viewHolder.touch.setOnClickListener(new View.OnClickListener() {
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
