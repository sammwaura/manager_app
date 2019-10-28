package com.usalamatechnology.manageapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usalamatechnology.manageapp.R;
import com.usalamatechnology.manageapp.models.ExpenseDetails;

import java.util.ArrayList;
import java.util.List;


public class ExpensesAdapter extends RecyclerView.Adapter <ExpensesAdapter.ViewHolder> {


    private Context context;

    private ExpensesAdapter.ItemClickListener itemClickListener;
    public ExpensesObserver mObserver;



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ItemClickListener itemClickListener;
        public TextView id, amount, category, notes, type;
        public CardView card;
        public LinearLayout touch;

        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener){

            super(itemView);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

        }
    }
    List<ExpenseDetails>posts;

    public ExpensesAdapter(Context context, List<ExpenseDetails> posts) {
        this.posts = posts;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.expensedetail, viewGroup, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }



    @Override
    public int getItemCount() {
        return posts.size();
    }



    public interface ItemClickListener {
    }


}
