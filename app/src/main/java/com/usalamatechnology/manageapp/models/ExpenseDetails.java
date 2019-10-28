package com.usalamatechnology.manageapp.models;

import com.android.volley.toolbox.StringRequest;

public class ExpenseDetails {

    String time;
    String id;
    String amount;
    String expense_type;
    String notes;


    public ExpenseDetails(String time, String id, String amount, String expense_type, String notes){
        this.time = time;
        this.id = id;
        this.amount = amount;
        this.expense_type = expense_type;
        this.notes = notes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpense_type() {
        return expense_type;
    }

    public void setExpense_type(String expense_type) {
        this.expense_type = expense_type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
