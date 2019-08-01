package com.usalamatechnology.manageapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class VehiclesRVAdapter extends RecyclerView.Adapter<VehiclesRVAdapter.PersonViewHolder> {

    public HomeIObserver mObserver;
    private String posterid;
    private String poster_type,amount,time,city,more,category,address;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;

        TextView amount;




        View linebar_green,linebar_orange;
        View dot_green,dot_orange;
        final RippleView rippleView;
        LinearLayout sendLayout;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            rippleView = (RippleView) itemView.findViewById(R.id.more3);

            amount = (TextView)itemView.findViewById(R.id.text_amount);



        }

    }

    public void setListener(HomeIObserver obs) { mObserver = obs; }

    List<HomePost> post;

    public VehiclesRVAdapter(List<HomePost> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicles_cardview, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    String type="";


    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {

        if(post.get(i).category.equalsIgnoreCase("vehicles"))
        {
            personViewHolder.amount.setText(post.get(i).amount);
        }

        posterid = post.get(i).id;
        poster_type = post.get(i).type;
        time = post.get(i).time;
        address = post.get(i).address;
        city = post.get(i).city;
        more = post.get(i).more;
        amount = post.get(i).amount;
        category= post.get(i).category;

        //personViewHolder.linearLayout.setGravity(Gravity.CENTER | Gravity.RIGHT);
        final int posi=i;
        personViewHolder.rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewHolder.rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        mObserver.onCardClicked(posi,posterid,poster_type,time,address,city,more,amount,category);
                    }
                });
            }
        });


    }
    @Override
    public int getItemCount() {
        return post.size();
    }
}