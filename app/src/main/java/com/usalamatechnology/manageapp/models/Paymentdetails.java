package com.usalamatechnology.manageapp.models;

public class Paymentdetails {

    private String id;
    private String number_plate;
    private String rate;
    private String destination;


    public Paymentdetails(String id,String number_plate, String rate, String destination) {
        this.id = id;
        this.number_plate = number_plate;
        this.rate = rate;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
