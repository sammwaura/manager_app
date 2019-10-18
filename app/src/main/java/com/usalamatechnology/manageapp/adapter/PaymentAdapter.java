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
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;

    private ItemClickListener itemClickListener;
    public PaymentObserver mObserver;


    private String numberplate_tapped;
    private String amount_tapped;


    public interface ItemClickListener {
        void onItemOnClick(View view, int position);

        View.OnClickListener onItemOnClick();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView number_plate, rate, destination;
        public CardView  card;
        public LinearLayout touch;
        public ItemClickListener itemClickListener;



        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            number_plate = itemView.findViewById(R.id.number_plate);
            destination = itemView.findViewById(R.id.destination);
            card = itemView.findViewById(R.id.card);
            touch = itemView.findViewById(R.id.touch);
            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemOnClick(v, getAdapterPosition());

        }
    }
    List<Paymentdetails>posts;

    public PaymentAdapter(Context context, PaymentObserver observer,List<Paymentdetails>posts){
        this.context = context;
        this.mObserver = observer;
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
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final Paymentdetails paymentdetail = posts.get(position);
        viewHolder.number_plate.setText(paymentdetail.getNumber_plate());
//        viewHolder.rate.setText(paymentdetail.getRate());
        viewHolder.destination.setText(paymentdetail.getDestination());


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
