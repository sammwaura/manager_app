package com.usalamatechnology.manageapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PassengerDetails  {

    private String name;
    private String phone_no;
    private String seat_no;

    public PassengerDetails(String name, String phone_no, String seat_no){
        this.name = name;
        this.phone_no = phone_no;
        this.seat_no = seat_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(String seat_no) {
        this.seat_no = seat_no;
    }
}