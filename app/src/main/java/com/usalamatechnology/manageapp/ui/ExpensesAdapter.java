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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView numberplate, time, amount, expense_type, notes;
        public CardView card;
        public LinearLayout touch;

        public ViewHolder(@NonNull View itemView){

            super(itemView);

            numberplate = itemView.findViewById(R.id.numberplate);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);
            expense_type = itemView.findViewById(R.id.type);
            notes = itemView.findViewById(R.id.notes);

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.numberplate.setText(posts.get(i).getNumberplate());
        viewHolder.time.setText(posts.get(i).getTime());
        viewHolder.amount.setText(posts.get(i).getAmount());
        viewHolder.expense_type.setText(posts.get(i).getExpense_type());
        viewHolder.notes.setText(posts.get(i).getNotes());

    }



    @Override
    public int getItemCount() {
        return posts.size();
    }



}
