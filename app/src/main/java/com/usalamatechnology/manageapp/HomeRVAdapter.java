package com.usalamatechnology.manageapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.andexert.library.RippleView;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.usalamatechnology.manageapp.HomePost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.TripViewHolder> {

    public HomeIObserver mObserver;
    AdapterView.OnItemClickListener itemClickListener;
    List<HomePost> homePosts;
    Context context;




    public static class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView number_plate,destination;
        TextView  amount;
        AdapterView.OnItemClickListener itemClickListener;



        TripViewHolder(View itemView, AdapterView.OnItemClickListener itemClickListener) {
            super(itemView);

            number_plate = itemView.findViewById(R.id.number_plate);
            destination = itemView.findViewById(R.id.destination);
            amount = itemView.findViewById(R.id.amount);
            this.itemClickListener = itemClickListener;

        }


        @Override
        public void onClick(View view) {
//            itemClickListener.onItemClick();

        }
    }

    public void setListener(HomeIObserver obs) { mObserver = obs; }


    List<HomePost> post;

    public HomeRVAdapter(List<HomePost> post){

        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_cardview, viewGroup, false);
        return new TripViewHolder(v, itemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int i) {

    }


    @Override
    public int getItemCount() {
        return post.size();
    }
}