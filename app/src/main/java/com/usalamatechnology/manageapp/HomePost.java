package com.usalamatechnology.manageapp;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class HomePost {

    public String id;
    public String amount;
    public String time;
    public String city;
    public String street;
    public String type;
    public String more;
    public String category;
    public String address;


    public HomePost(String id, String amount, String time, String city, String street, String type, String more, String category, String address
                       ) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.time = time;
        this.city = city;
        this.street = street;
        this.more = more;
        this.category = category;
        this.address = address;


    }

}