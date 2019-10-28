package com.usalamatechnology.manageapp.models;

import com.android.volley.toolbox.StringRequest;

public class ExpenseDetails {

    String id;
    String amount;
    String type;
    String notes;


    public ExpenseDetails(String s, String id, String amount, String type, String notes){
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.notes = notes;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
