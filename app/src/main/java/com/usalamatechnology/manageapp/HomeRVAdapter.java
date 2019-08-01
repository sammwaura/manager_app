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
import com.usalamatechnology.manageapp.HomePost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.PersonViewHolder> {

    public HomeIObserver mObserver;
    private String posterid;
    private String poster_type,amount,time,city,more,category,address;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView time;
        TextView location;
        TextView amount;
        TextView the_id;

        TextView text_expense;
        TextView approve_trip,edit_rate;
        TextView text_earnings;
        ImageView text_approve;

        View linebar_green,linebar_orange;
        View dot_green,dot_orange;
        final RippleView rippleView;
        LinearLayout sendLayout;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            rippleView = (RippleView) itemView.findViewById(R.id.more3);
            name = (TextView)itemView.findViewById(R.id.text_name);
            approve_trip= (TextView)itemView.findViewById(R.id.approve_trip);
            edit_rate= (TextView)itemView.findViewById(R.id.edit_rate);
            text_approve= (ImageView) itemView.findViewById(R.id.text_approve);
            sendLayout=(LinearLayout) itemView.findViewById(R.id.linear_trip_on);

            text_expense = (TextView)itemView.findViewById(R.id.text_expense);
            text_earnings = (TextView)itemView.findViewById(R.id.text_earnings);

            time = (TextView)itemView.findViewById(R.id.text_time);
            location = (TextView)itemView.findViewById(R.id.location);
            amount = (TextView)itemView.findViewById(R.id.text_amount);
            the_id = (TextView)itemView.findViewById(R.id.the_id);

            linebar_green = (View) itemView.findViewById(R.id.bar_green);
            linebar_orange = (View)itemView.findViewById(R.id.bar_orange);

            dot_green = (View)itemView.findViewById(R.id.circle_green);
            dot_orange = (View)itemView.findViewById(R.id.circle_orange);


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
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_cardview, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    String type="";
    String is_approved="";
    String time_end="";

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {

     if(post.get(i).category.equalsIgnoreCase("Trip"))
        {
            type= post.get(i).type.split("#")[0];
            is_approved= post.get(i).type.split("#")[1];
            time_end= post.get(i).type.split("#")[5];
            if(type.equalsIgnoreCase("1"))
            {
                //personViewHolder.text_earnings.setText("Trip");
                personViewHolder.text_earnings.setVisibility(View.VISIBLE);

                personViewHolder.dot_green.setVisibility(View.VISIBLE);
                personViewHolder.linebar_green.setVisibility(View.VISIBLE);

                personViewHolder.text_expense.setVisibility(View.GONE);
                personViewHolder.dot_orange.setVisibility(View.GONE);
                personViewHolder.linebar_orange.setVisibility(View.GONE);

                if(time_end.equals("0"))
                {
                    personViewHolder.sendLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    personViewHolder.sendLayout.setVisibility(View.GONE);
                }

                //personViewHolder.name.setText("Trip ID: "+post.get(i).id);
            }
            else if(type.equalsIgnoreCase("0"))
            {
                //personViewHolder.text_earnings.setText("Trip");
                personViewHolder.text_earnings.setVisibility(View.GONE);
                personViewHolder.dot_green.setVisibility(View.GONE);
                personViewHolder.linebar_green.setVisibility(View.GONE);
                personViewHolder.text_expense.setVisibility(View.VISIBLE);
                personViewHolder.text_expense.setText("Trip");
                personViewHolder.dot_orange.setVisibility(View.VISIBLE);
                personViewHolder.linebar_orange.setVisibility(View.VISIBLE);
                //personViewHolder.name.setText("Trip ID: "+post.get(i).id);
                personViewHolder.approve_trip.setVisibility(View.GONE);
                personViewHolder.edit_rate.setVisibility(View.GONE);
                personViewHolder.text_approve.setVisibility(View.GONE);

                if(time_end.equals("0"))
                {
                    personViewHolder.sendLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    personViewHolder.sendLayout.setVisibility(View.GONE);
                }
            }
        }
     else if(post.get(i).category.equalsIgnoreCase("Expense"))
     {
         personViewHolder.text_expense.setVisibility(View.VISIBLE);
         personViewHolder.dot_orange.setVisibility(View.VISIBLE);
         personViewHolder.linebar_orange.setVisibility(View.VISIBLE);

         personViewHolder.text_earnings.setVisibility(View.GONE);
         personViewHolder.dot_green.setVisibility(View.GONE);
         personViewHolder.linebar_green.setVisibility(View.GONE);

         personViewHolder.name.setText(post.get(i).type);
     }




        long unixSeconds = Long.parseLong(post.get(i).time);

        long timeInMillis = System.currentTimeMillis();

        if((((timeInMillis/1000) - unixSeconds)/(3600*24)) >1)
        {
            // convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds*1000L);
// the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("h.mm a : EEEE dd MMMM, yyyy");
// give a timezone reference for formatting (see comment at the bottom)
            //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);
            //System.out.println(formattedDate);

            personViewHolder.time.setText(formattedDate);
        }
        else
        {
            String text = TimeAgo.using(unixSeconds*1000L);

            // convert seconds to milliseconds
            Date date = new java.util.Date(unixSeconds*1000L);
// the format of your date
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("h.mm a");
// give a timezone reference for formatting (see comment at the bottom)
            //sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);
            //System.out.println(formattedDate);

            String s1 = text.substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + text.substring(1);

            personViewHolder.time.setText(nameCapitalized+" : "+formattedDate);
        }

        personViewHolder.location.setText(post.get(i).address);

        String s="";
        try {
            // The comma in the format specifier does the trick
            s = String.format("%,d", Long.parseLong(post.get(i).amount));
        } catch (NumberFormatException e) {
        }

        String total = "Kes. "+s;


        if(post.get(i).category.equalsIgnoreCase("Trip")) {
            Random rand = new Random();

            //int  n = rand.nextInt(5) + 1;
            personViewHolder.amount.setText(post.get(i).amount);

            personViewHolder.name.setText("Trip ID: "+post.get(i).id);

            if(!post.get(i).more.equals("0"))
            {
                //personViewHolder.text_earnings.setText("Rate @ Ksh."+post.get(i).more);

                String the_type= post.get(i).type.split("#")[0];
                is_approved= post.get(i).type.split("#")[1];

                personViewHolder.text_earnings.setText("Rate @ Ksh."+post.get(i).more);

                if(is_approved.equals("1") && the_type.equals("1"))
                {
                    personViewHolder.approve_trip.setVisibility(View.GONE);
                    personViewHolder.edit_rate.setVisibility(View.VISIBLE);
                    personViewHolder.text_approve.setVisibility(View.VISIBLE);
                }
                else if (is_approved.equals("0") && the_type.equals("1"))
                {
                    personViewHolder.approve_trip.setVisibility(View.VISIBLE);
                    personViewHolder.text_approve.setVisibility(View.GONE);
                    personViewHolder.edit_rate.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                personViewHolder.text_earnings.setText("Trip");
                personViewHolder.approve_trip.setVisibility(View.GONE);
                personViewHolder.edit_rate.setVisibility(View.GONE);
                personViewHolder.text_approve.setVisibility(View.GONE);
            }

        }
        else
        {
            personViewHolder.amount.setText(total);
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


        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onCardClicked(posi,posterid,poster_type,time,address,city,more,amount,category);
            }
        });

        personViewHolder.approve_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onCardApproved(posi,posterid,poster_type,time,address,city,more,amount,category);
            }
        });

        personViewHolder.edit_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onEditRate(posi,posterid,poster_type,time,address,city,more,amount,category);
            }
        });

    }
    @Override
    public int getItemCount() {
        return post.size();
    }
}